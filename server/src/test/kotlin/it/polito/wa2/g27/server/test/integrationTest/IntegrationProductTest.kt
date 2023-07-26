package it.polito.wa2.g27.server.test.integrationTest
import it.polito.wa2.g27.server.products.ProductDTO
import it.polito.wa2.g27.server.products.ProductRepository
import it.polito.wa2.g27.server.products.toProduct
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IntegrationProductTest {
    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:latest")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.jpa.hibernate.ddl-auto") { "create-drop" }
        }

    }

    @LocalServerPort
    protected var port: Int = 0

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    val prod1 = ProductDTO("A01", "Lego Star Wars", "LEGO", "Space");
    val prod2 = ProductDTO("A02", "Lego Indiana Jones", "LEGO", "Historical");
    val prod3 = ProductDTO("B01", "Lego Duplo Fashion Blogger", "LEGO Duplo", "Fashion");
    val prod4 = ProductDTO("B02", "Lego Dublo TikToker", "LEGO Duplo", "Influencer")

    @Autowired
    private lateinit var productRepository: ProductRepository
    @BeforeEach
    fun insertData() {
        productRepository.deleteAll()

        val product1 = prod1.toProduct()
        val product2 = prod2.toProduct()
        val product3 = prod3.toProduct()
        val product4 = prod4.toProduct()

        productRepository.save(product1)
        productRepository.save(product2)
        productRepository.save(product3)
        productRepository.save(product4)
    }

    @Test
    fun getProducts() {
        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/public/products",
            HttpMethod.GET,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun getProductByIdCorrectId() {
        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/public/products/${prod1.id}",
            HttpMethod.GET,
            requestEntity,
            ProductDTO::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertEquals(response.body, prod1)
    }

    @Test
    fun getProductByIdNoId() {
        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/public/products/10",
            HttpMethod.GET,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

}
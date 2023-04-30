package it.polito.wa2.g27.server.integrationTests;

import it.polito.wa2.g27.server.products.ProductDTO
import it.polito.wa2.g27.server.products.ProductService
import it.polito.wa2.g27.server.profiles.ProfileDTO
import it.polito.wa2.g27.server.profiles.ProfileService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.sql.DriverManager


@Testcontainers
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class ProductTests {
    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:latest")
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.jpa.hibernate.ddl-auto") {"create-drop"}
        }
    }
    @LocalServerPort
    protected var port: Int = 0
    @Autowired
    lateinit var productService: ProductService

    @BeforeEach
    fun populateDB() {
        DriverManager.getConnection(postgres.jdbcUrl, postgres.username, postgres.password).use { conn ->
            val script = File("src/test/resources/schema_populate.sql").readText()
            conn.createStatement().use { stmt ->
                stmt.execute(script)
            }
        }
    }

    @Test
    fun getProducts() {
        val products = productService.getAll()

        val expectedProducts = listOf(
            ProductDTO("A01", "Lego Star Wars", "LEGO", "Space"),
            ProductDTO("A02", "Lego Indiana Jones", "LEGO", "Historical"),
            ProductDTO("B01", "Lego Duplo Fashion Blogger", "LEGO Duplo", "Fashion"),
            ProductDTO("B02", "Lego Dublo TikToker", "LEGO Duplo", "Influencer")
        )

        Assertions.assertEquals(expectedProducts.toSet(), products.toSet())
    }

    @Test
    fun getProductById() {
        val product = productService.getProduct("B01")
        
        val expectedProduct = ProductDTO("B01", "Lego Duplo Fashion Blogger", "LEGO Duplo", "Fashion");

        Assertions.assertEquals(expectedProduct, product)
    }

}

/*

//Write here your integration test
val testRestTemplate = TestRestTemplate()
val response: ResponseEntity<String> = testRestTemplate.getForEntity<String>(
    "http://localhost:8080/profiles/pollo2.matano@gmail.com",
    String
)

 */
package it.polito.wa2.g27.server.test.serviceTest;

import it.polito.wa2.g27.server.exceptions.ProductNotFoundException
import it.polito.wa2.g27.server.products.ProductDTO
import it.polito.wa2.g27.server.products.ProductRepository
import it.polito.wa2.g27.server.products.ProductService
import it.polito.wa2.g27.server.products.toProduct
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

    val prod1 = ProductDTO("A01", "Lego Star Wars", "LEGO", "Space");
    val prod2 = ProductDTO("A02", "Lego Indiana Jones", "LEGO", "Historical");
    val prod3 = ProductDTO("B01", "Lego Duplo Fashion Blogger", "LEGO Duplo", "Fashion");
    val prod4 = ProductDTO("B02", "Lego Dublo TikToker", "LEGO Duplo", "Influencer")
    @Autowired
    private lateinit var productRepository: ProductRepository

    @BeforeEach
    fun populateDB() {
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
        val products = productService.getAll()

        val expectedProducts = listOf(
            prod1, prod2, prod3, prod4
        )

        Assertions.assertEquals(expectedProducts.toSet(), products.toSet())
    }

    @Test
    fun getProductById() {
        val product = productService.getProduct(prod3.id)
        
        val expectedProduct = prod3

        Assertions.assertEquals(expectedProduct, product)
    }

    @Test
    fun getProductByIdNotExist() {

        Assertions.assertThrows(ProductNotFoundException::class.java, {
            productService.getProduct("C01")
        }, "Product Not Found")
    }

}

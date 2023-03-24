package it.polito.wa2.g27.server.products

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(private val productService: ProductService) {

    @GetMapping("/products/")
    fun getAll(): List<ProductDTO> {
        return productService.getAll()
    }

    @GetMapping("/products/{productId}")
    fun getProduct(@PathVariable productId: String) : ProductDTO? {
        return productService.getProduct(productId)
    }
}
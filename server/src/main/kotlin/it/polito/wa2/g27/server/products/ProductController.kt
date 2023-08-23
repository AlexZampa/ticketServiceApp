package it.polito.wa2.g27.server.products

import io.micrometer.observation.annotation.Observed;
import it.polito.wa2.g27.server.ticket.TicketDTO
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@Observed
class ProductController(private val productService: ProductService) {


    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/public/products")
    @Observed
    fun getAll(): List<ProductDTO> {
        return productService.getAll()
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/public/products/{productId}")
    @Observed
    fun getProduct(@PathVariable productId: String) : ProductDTO? {
        return productService.getProduct(productId)
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PostMapping("/manager/products")
    @ResponseStatus(HttpStatus.CREATED)
    fun postCreateTicket(@Valid @RequestBody productDTO: ProductDTO) {
        productService.createProduct(productDTO)
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PutMapping("/manager/products/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun putModifyPriority(@PathVariable productId: String, @Valid @RequestBody productDTO: ProductDTO) {
        productService.modifyProduct(productId, productDTO)
    }

}
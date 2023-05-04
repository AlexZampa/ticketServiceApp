package it.polito.wa2.g27.server.products

import it.polito.wa2.g27.server.exceptions.ProductNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(private val productRepository: ProductRepository): ProductService {
    override fun getAll(): List<ProductDTO> {
        return productRepository.findAll().map {it.toDTO()}
    }

    override fun getProduct(productId: String): ProductDTO {
        return productRepository.findByIdOrNull(productId)?.toDTO() ?: throw ProductNotFoundException("Product Not Found")
    }


}
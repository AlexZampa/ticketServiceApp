package it.polito.wa2.g27.server.products

import it.polito.wa2.g27.server.exceptions.ProductNotFoundException
import it.polito.wa2.g27.server.exceptions.ProfileNotFoundException
import it.polito.wa2.g27.server.exceptions.TicketNotFoundException
import it.polito.wa2.g27.server.profiles.Profile
import it.polito.wa2.g27.server.ticket.Ticket
import it.polito.wa2.g27.server.ticket.TicketDTO
import it.polito.wa2.g27.server.ticket.toDTO
import it.polito.wa2.g27.server.ticket.toTicket
import it.polito.wa2.g27.server.ticketHistory.TicketHistory
import it.polito.wa2.g27.server.ticketHistory.TicketStatus
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

    override fun createProduct(productDTO: ProductDTO) : ProductDTO {
        val product = productDTO.toProduct()
        val newProduct = productRepository.save(product)
        return newProduct.toDTO()
    }

    override fun modifyProduct(productId: String, productDTO: ProductDTO): ProductDTO {

        val product = productDTO.toProduct()
        val ticket = productRepository.findByIdOrNull(productId) ?: throw ProductNotFoundException("Product Not Found")
        val newProduct = productRepository.save(product)
        return newProduct.toDTO()
    }
}
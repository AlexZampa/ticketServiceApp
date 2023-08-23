package it.polito.wa2.g27.server.products

import it.polito.wa2.g27.server.ticket.TicketDTO

interface ProductService {
    fun getAll() : List<ProductDTO>

    fun getProduct(productId: String) : ProductDTO

    fun createProduct(productDTO: ProductDTO) : ProductDTO

    fun modifyProduct(productId: String, productDTO: ProductDTO): ProductDTO

}
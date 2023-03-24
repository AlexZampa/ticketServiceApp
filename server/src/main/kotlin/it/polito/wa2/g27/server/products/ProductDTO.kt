package it.polito.wa2.g27.server.products

data class ProductDTO(
    val id: String,
    val name: String,
    val brand: String,
    val description: String
)

/* Extension function of Product to remove the annotations */
fun Product.toDTO(): ProductDTO {
    return ProductDTO(id, name, brand, description)
}

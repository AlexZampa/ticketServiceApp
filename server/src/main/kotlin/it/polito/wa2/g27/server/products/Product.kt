package it.polito.wa2.g27.server.products

import it.polito.wa2.g27.server.messages.Message
import it.polito.wa2.g27.server.ticket.Ticket
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "products")
class Product {
    @Id
    var id : String = ""
    var name : String = ""
    var brand : String = ""
    var description: String = ""
    @OneToMany(mappedBy = "product")
    var tickets : MutableSet<Ticket> = mutableSetOf()

    fun addTicket(t: Ticket) {
        t.product = this
        tickets.add(t)
    }
}

fun ProductDTO.toProduct(): Product{
    val p = Product()
    p.id = id
    p.name = name
    p.brand = brand
    p.description = description
    return p
}
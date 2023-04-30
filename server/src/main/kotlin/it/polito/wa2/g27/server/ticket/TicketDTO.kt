package it.polito.wa2.g27.server.ticket

import it.polito.wa2.g27.server.ticketHistory.TicketStatus
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class TicketDTO(
    val id : Int,
    @field:NotEmpty(message = "productId can not be blank")
    @field:NotNull(message = "productId can not be blank")
    val productId : String?,
    @field:NotEmpty(message = "category can not be blank")
    @field:NotNull(message = "category can not be blank")
    val category: String,
    val priority: Int?,
    @field:NotEmpty(message = "description can not be blank")
    @field:NotNull(message = "description can not be blank")
    val description: String,
    @field:NotNull(message = "profileId can not be blank")
    val profileId: Int?,
    val expertId: Int?,
    @field:Enumerated(EnumType.STRING)
    val status: TicketStatus?
)

fun Ticket.toDTO(): TicketDTO {
    var status = TicketStatus.valueOf(ticketHistory.sortedByDescending { it.date }[0].status)
    println(this.product.toString())
    return TicketDTO(id, product?.id, category, priority, description, profile?.id, expert?.id, status)
}

fun convertToDTO(ticket : Ticket): TicketDTO {
    var status = TicketStatus.valueOf(ticket.ticketHistory.sortedByDescending { it.date }[0].status)
    println(ticket.product.toString())
    return TicketDTO(ticket.id, ticket.product?.id, ticket.category, ticket.priority, ticket.description, ticket.profile?.id, ticket.expert?.id, status)
}


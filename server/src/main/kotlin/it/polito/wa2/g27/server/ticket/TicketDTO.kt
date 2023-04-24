package it.polito.wa2.g27.server.ticket

data class TicketDTO(
    val id : Int,
    val productId : String?,
    val category: String,
    val priority: Int,
    val description: String,
    val profileId: String?,
    val expertId: String?,
    val chatId: String,
    val status: String
)

fun Ticket.toDTO(): TicketDTO {
    var status = ticketHistory.sortedByDescending { it.date }[0].status
    return TicketDTO(id, product?.id, category, priority, description, profile?.email, expert?.email, chatId, status)
}

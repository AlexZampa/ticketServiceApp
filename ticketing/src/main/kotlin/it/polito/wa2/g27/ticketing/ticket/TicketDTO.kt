package it.polito.wa2.g27.ticketing.ticket

data class TicketDTO(
    var id : Int,
    var productId : String,
    var category: String,
    var priority: Int,
    var description: String,
    var userId: String,
    var expertId: String,
    var chatId: String,
    var status: String
)

fun Ticket.toDTO(): TicketDTO {
    var status = ticketHistory.sortedByDescending { it.date }[0].status
    return TicketDTO(id, productId, category, priority, description, userId, expertId, chatId, status)
}

package it.polito.wa2.g27.server.ticket

data class TicketDTO(
    var id : Int,
    var productId : String?,
    var category: String,
    var priority: Int,
    var description: String,
    var profileId: String?,
    var expertId: String,
    var chatId: String,
    var status: String
)

fun Ticket.toDTO(): TicketDTO {
    var status = ticketHistory.sortedByDescending { it.date }[0].status
    return TicketDTO(id, product?.id, category, priority, description, profile?.email, expertId, chatId, status)
}

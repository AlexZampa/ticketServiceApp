package it.polito.wa2.g27.server.ticketHistory

import java.time.LocalDateTime

data class TicketHistoryDTO(
    var id : Int,
    var ticketId: Int?,
    var status: String,
    var date: LocalDateTime,
)

fun TicketHistory.toDTO(): TicketHistoryDTO {
    return TicketHistoryDTO(id, ticket?.id, status, date)
}

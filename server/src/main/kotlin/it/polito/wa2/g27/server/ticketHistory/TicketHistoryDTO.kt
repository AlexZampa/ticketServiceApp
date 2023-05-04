package it.polito.wa2.g27.server.ticketHistory

import java.time.LocalDateTime

data class TicketHistoryDTO(
    val id : Int,
    val ticketId: Int?,
    val status: String,
    val date: LocalDateTime,
)

fun TicketHistory.toDTO(): TicketHistoryDTO {
    return TicketHistoryDTO(id, ticket?.id, status, date)
}

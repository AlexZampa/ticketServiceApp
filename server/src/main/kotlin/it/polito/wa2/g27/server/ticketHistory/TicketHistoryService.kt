package it.polito.wa2.g27.server.ticketHistory

interface TicketHistoryService {
    fun insertNewTicketStatus()
    fun getAllTicketHistory(): List<TicketHistory>
}
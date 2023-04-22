package it.polito.wa2.g27.ticketing.ticket

interface TicketHistoryService {
    fun insertNewTicketStatus()
    fun getAllTicketHistory(): List<TicketHistory>
}
package it.polito.wa2.g27.server.ticket

import it.polito.wa2.g27.server.ticket.TicketDTO

interface TicketService {
    fun getTickets(): List<TicketDTO>
    fun getSingleTicket(id: Int): TicketDTO?
    fun createTicket(ticket: TicketDTO)
    fun modifyPriority(ticketId: Int, priority: String)
    fun modifyExpert(ticketId: Int, expertId: String)
    fun modifyStatus(ticketId: Int, status: String)
    fun modifyTicket(ticketId: Int, ticket: TicketDTO)
}
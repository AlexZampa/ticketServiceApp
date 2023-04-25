package it.polito.wa2.g27.server.ticket

import it.polito.wa2.g27.server.products.ProductDTO
import it.polito.wa2.g27.server.profiles.ProfileDTO
import it.polito.wa2.g27.server.ticket.TicketDTO

interface TicketService {
    fun getOpenTickets(): List<TicketDTO>
    fun getTicketsByProfile(profileDTO: ProfileDTO): List<TicketDTO>
    fun getAssignedTickets(profileDTO: ProfileDTO): List<TicketDTO>
    fun getSingleTicket(id: Int): TicketDTO?
    fun createTicket(ticketDTO: TicketDTO)
    fun modifyPriority(ticketId: Int, priority: String)
    fun modifyExpert(ticketId: Int, expertId: String)
    fun modifyStatus(ticketId: Int, status: String)
    fun modifyTicket(ticketId: Int, ticket: TicketDTO)
}
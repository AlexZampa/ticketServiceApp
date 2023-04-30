package it.polito.wa2.g27.server.ticket

import it.polito.wa2.g27.server.profiles.ProfileDTO

interface TicketService {
    fun getOpenTickets(): List<TicketDTO>
    fun getTicketsByProfile(profileDTO: ProfileDTO): List<TicketDTO>
    fun getAssignedTickets(profileDTO: ProfileDTO): List<TicketDTO>
    fun getSingleTicket(id: Int): TicketDTO?
    fun createTicket(ticketDTO: TicketDTO) : TicketDTO
    fun modifyPriority(ticketId: Int, priority: Int)
    fun assignExpert(ticketId: Int, expertEmail: String, priority: Int)
    fun closeTicket(ticketId: Int)
    fun stopTicketProgress(ticketId: Int)
    fun resolveTicketIssue(ticketId: Int)
    fun reopenTicket(ticketId: Int)


}
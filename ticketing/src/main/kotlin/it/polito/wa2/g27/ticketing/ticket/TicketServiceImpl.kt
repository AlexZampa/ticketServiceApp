package it.polito.wa2.g27.ticketing.ticket

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TicketServiceImpl(private val ticketRepository: TicketRepository): TicketService {
    override fun getTickets(): List<TicketDTO> {
        TODO("Not yet implemented")
    }

    override fun getSingleTicket(id: Int): TicketDTO? {
        return ticketRepository.findByIdOrNull(id)?.toDTO()
    }

    override fun createTicket(ticket: TicketDTO) {
        TODO("Not yet implemented")
    }

    override fun modifyPriority(ticketId: Int, priority: String) {
        TODO("Not yet implemented")
    }

    override fun modifyExpert(ticketId: Int, expertId: String) {
        TODO("Not yet implemented")
    }

    override fun modifyStatus(ticketId: Int, status: String) {
        TODO("Not yet implemented")
    }

    override fun modifyTicket(ticketId: Int, ticket: TicketDTO) {
        TODO("Not yet implemented")
    }
}
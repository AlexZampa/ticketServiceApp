package it.polito.wa2.g27.server.ticket

import it.polito.wa2.g27.server.exceptions.*
import it.polito.wa2.g27.server.products.Product
import it.polito.wa2.g27.server.products.ProductRepository
import it.polito.wa2.g27.server.profiles.Profile
import it.polito.wa2.g27.server.profiles.ProfileDTO
import it.polito.wa2.g27.server.profiles.ProfileRepository
import it.polito.wa2.g27.server.ticketHistory.TicketHistory
import it.polito.wa2.g27.server.ticketHistory.TicketHistoryRepository
import it.polito.wa2.g27.server.ticketHistory.TicketStatus
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TicketServiceImpl(private val ticketRepository: TicketRepository,
    private val profileRepository: ProfileRepository,
    private val productRepository: ProductRepository,
    private val ticketHistoryRepository: TicketHistoryRepository
): TicketService {
    override fun getOpenTickets(): List<TicketDTO> {
        return ticketRepository.findAll().filter {ticket -> ticket.ticketHistory.sortedByDescending { it.date }[0].status == TicketStatus.OPEN.name}
            .map { it.toDTO() }
    }

    override fun getTicketsByProfile(profileDTO: ProfileDTO): List<TicketDTO> {
        return ticketRepository.findAllByProfileId(profileDTO.id!!).map { it.toDTO() }
    }

    override fun getAssignedTickets(profileDTO: ProfileDTO): List<TicketDTO> {
        return ticketRepository.findAllByExpertId(profileDTO.id!!).map { it.toDTO() }
    }

    override fun getSingleTicket(id: Int): TicketDTO? {
        return ticketRepository.findByIdOrNull(id)?.toDTO()
    }

    @Transactional
    override fun createTicket(ticketDTO: TicketDTO) {
        val product: Product = productRepository.findByIdOrNull(ticketDTO.productId!!) ?: throw ProductNotFoundException("Product Not Found")
        val profile: Profile = profileRepository.findByIdOrNull(ticketDTO.profileId!!) ?: throw ProfileNotFoundException("Profile not found")
        val ticketHistory = TicketHistory()
        ticketHistory.status = TicketStatus.OPEN.name
        val ticket: Ticket = ticketDTO.toTicket()
        profile.addTicketCreated(ticket)
        product.addTicket(ticket)
        ticket.addTicketHistory(ticketHistory)
        ticketRepository.save(ticket)
        ticketHistoryRepository.save(ticketHistory)
    }

    override fun modifyPriority(ticketId: Int, priority: Int) {
        if(priority < 1 || priority > 3){
            throw TicketPriorityNotValidException("Priority Invalid")
        }
        val ticket = ticketRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket Not Found")
        ticket.priority = priority
        ticketRepository.save(ticket)
    }

    override fun assignExpert(ticketId: Int, expertEmail: String) {
        val ticket = ticketRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket Not Found")
        val expert = profileRepository.findByEmail(expertEmail) ?: throw ProfileNotFoundException("Profile Not Found")
        val prevStatus = ticket.ticketHistory.sortedByDescending { it.date }[0].status
        if( prevStatus != TicketStatus.OPEN.name && prevStatus != TicketStatus.REOPENED.name){
            throw TicketStatusException("The Ticket is not Opened or Reopened")
        }
        val ticketHistory = TicketHistory()
        ticketHistory.status = TicketStatus.PROGRESS.name
        expert.addTicketAssigned(ticket)
        ticket.addTicketHistory(ticketHistory)
        ticketRepository.save(ticket)
        ticketHistoryRepository.save(ticketHistory)
    }

    override fun modifyStatus(ticketId: Int, status: String) {
        try {
            val newStatus = TicketStatus.valueOf(status)
            val ticket = ticketRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket Not Found")
            val ticketHistory = TicketHistory()
            ticketHistory.status = newStatus.name
            ticket.addTicketHistory(ticketHistory)
            ticketHistoryRepository.save(ticketHistory)
        }
        catch (e:IllegalArgumentException){
            throw TicketStatusNotValidException("Wrong Ticket Status")
        }

    }

    override fun closeTicket(ticketId: Int) {
        val ticket = ticketRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket Not Found")
        ticket.expert = null
        ticket.priority = 0
        if(ticket.ticketHistory.sortedByDescending { it.date }[0].status == TicketStatus.CLOSED.name){
            throw TicketStatusException("Ticket already closed")
        }
        val ticketHistory = TicketHistory()
        ticketHistory.status = TicketStatus.CLOSED.name
        ticket.addTicketHistory(ticketHistory)
        ticketRepository.save(ticket)
        ticketHistoryRepository.save(ticketHistory)
    }

}
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

@Service @Transactional
class TicketServiceImpl(private val ticketRepository: TicketRepository,
    private val profileRepository: ProfileRepository,
    private val productRepository: ProductRepository,
    private val ticketHistoryRepository: TicketHistoryRepository
): TicketService {

    @Transactional(readOnly = true)
    override fun getAllTickets(): List<TicketDTO> {
        return ticketRepository.findAll()
            .map { it.toDTO() }
    }

    @Transactional(readOnly = true)
    override fun getOpenTickets(): List<TicketDTO> {
        return ticketRepository.findAll().filter {ticket -> ticket.ticketHistory.sortedByDescending { it.date }[0].status == TicketStatus.OPEN.name
                || ticket.ticketHistory.sortedByDescending { it.date }[0].status == TicketStatus.REOPENED.name}
            .map { it.toDTO() }
    }

    @Transactional(readOnly = true)
    override fun getTicketsByProfile(profileDTO: ProfileDTO): List<TicketDTO> {
        return ticketRepository.findAllByProfileId(profileDTO.id!!).map { it.toDTO() }
    }

    @Transactional(readOnly = true)
    override fun getAssignedTickets(profileDTO: ProfileDTO): List<TicketDTO> {
        return ticketRepository.findAllByExpertId(profileDTO.id!!).map { it.toDTO() }
    }

    @Transactional(readOnly = true)
    override fun getSingleTicket(id: Int): TicketDTO {
        return ticketRepository.findByIdOrNull(id)?.toDTO() ?: throw TicketNotFoundException("Ticket Not Found")
    }

    override fun createTicket(ticketDTO: TicketDTO) : TicketDTO {
        val product: Product = productRepository.findByIdOrNull(ticketDTO.productId!!) ?: throw ProductNotFoundException("Product Not Found")
        val profile: Profile = profileRepository.findByIdOrNull(ticketDTO.profileId!!) ?: throw ProfileNotFoundException("Profile not found")
        val ticketHistory = TicketHistory()
        ticketHistory.status = TicketStatus.OPEN.name
        val ticket: Ticket = ticketDTO.toTicket()
        profile.addTicketCreated(ticket)
        product.addTicket(ticket)
        ticket.addTicketHistory(ticketHistory)
        val newTicket = ticketRepository.save(ticket)
        ticketHistoryRepository.save(ticketHistory)
        return newTicket.toDTO()
    }

    override fun modifyPriority(ticketId: Int, priority: Int) {
        if(priority < 1 || priority > 3){
            throw TicketPriorityNotValidException("Priority Invalid")
        }
        val ticket = ticketRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket Not Found")
        ticket.priority = priority
        ticketRepository.save(ticket)
    }

    override fun assignExpert(ticketId: Int, expertEmail: String, priority: Int) {
        if(priority < 1 || priority > 3) throw TicketPriorityNotValidException("Priority Invalid")
        val ticket = ticketRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket Not Found")
        val expert = profileRepository.findByEmail(expertEmail) ?: throw ProfileNotFoundException("Profile Not Found")
        val prevStatus = ticket.ticketHistory.sortedByDescending { it.date }[0].status
        if( prevStatus != TicketStatus.OPEN.name && prevStatus != TicketStatus.REOPENED.name) throw TicketStatusException("The Ticket is not Opened or Reopened")
        ticket.priority = priority
        val updatedTicketHistory = TicketHistory()
        updatedTicketHistory.status = TicketStatus.PROGRESS.name
        expert.addTicketAssigned(ticket)
        ticket.addTicketHistory(updatedTicketHistory)
        ticketRepository.save(ticket)
        ticketHistoryRepository.save(updatedTicketHistory)
    }

    override fun stopTicketProgress(ticketId: Int) {
        val ticket = ticketRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket Not Found")
        ticket.expert = null
        ticket.priority = 0
        if(ticket.ticketHistory.sortedByDescending { it.date }[0].status != TicketStatus.PROGRESS.name){
            throw TicketStatusException("Ticket not in Progress")
        }
        val ticketHistory = TicketHistory()
        ticketHistory.status = TicketStatus.OPEN.name
        ticket.addTicketHistory(ticketHistory)
        ticketRepository.save(ticket)
        ticketHistoryRepository.save(ticketHistory)
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

    override fun resolveTicketIssue(ticketId: Int) {
        val ticket = ticketRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket Not Found")
        ticket.expert = null
        ticket.priority = 0
        if(ticket.ticketHistory.sortedByDescending { it.date }[0].status == TicketStatus.CLOSED.name){
            throw TicketStatusException("Ticket is Closed")
        }
        val ticketHistory = TicketHistory()
        ticketHistory.status = TicketStatus.RESOLVED.name
        ticket.addTicketHistory(ticketHistory)
        ticketRepository.save(ticket)
        ticketHistoryRepository.save(ticketHistory)
    }

    override fun reopenTicket(ticketId: Int) {
        val ticket = ticketRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket Not Found")
        ticket.expert = null
        ticket.priority = 0
        val prevStatus = ticket.ticketHistory.sortedByDescending { it.date }[0].status
        if( prevStatus != TicketStatus.CLOSED.name && prevStatus != TicketStatus.RESOLVED.name)
            throw TicketStatusException("The Ticket is not Closed or Resolved")
        val ticketHistory = TicketHistory()
        ticketHistory.status = TicketStatus.REOPENED.name
        ticket.addTicketHistory(ticketHistory)
        ticketRepository.save(ticket)
        ticketHistoryRepository.save(ticketHistory)
    }

}
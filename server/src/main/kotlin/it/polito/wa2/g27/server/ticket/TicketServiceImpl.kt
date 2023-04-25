package it.polito.wa2.g27.server.ticket

import it.polito.wa2.g27.server.exceptions.ProductNotFoundException
import it.polito.wa2.g27.server.exceptions.ProfileNotFoundException
import it.polito.wa2.g27.server.products.Product
import it.polito.wa2.g27.server.products.ProductRepository
import it.polito.wa2.g27.server.profiles.Profile
import it.polito.wa2.g27.server.profiles.ProfileDTO
import it.polito.wa2.g27.server.profiles.ProfileRepository
import it.polito.wa2.g27.server.ticketHistory.TicketStatus
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TicketServiceImpl(private val ticketRepository: TicketRepository,
    private val profileRepository: ProfileRepository,
    private val productRepository: ProductRepository
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

    override fun createTicket(ticketDTO: TicketDTO) {
        val product: Product = productRepository.findByIdOrNull(ticketDTO.productId!!) ?: throw ProductNotFoundException("Product Not Found")
        val profile: Profile = profileRepository.findByIdOrNull(ticketDTO.profileId!!) ?: throw ProfileNotFoundException("Profile not found")
        val ticket: Ticket = ticketDTO.toTicket()
        profile.addTicketCreated(ticket)
        product.addTicket(ticket)
        ticketRepository.save(ticket)
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
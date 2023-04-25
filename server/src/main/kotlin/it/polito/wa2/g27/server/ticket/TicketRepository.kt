package it.polito.wa2.g27.server.ticket

import it.polito.wa2.g27.server.profiles.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository: JpaRepository<Ticket, Int> {
    fun findAllByProfileId(profileId: Int): List<Ticket>
    fun findAllByExpertId(expertId: Int): List<Ticket>
}
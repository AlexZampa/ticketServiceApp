package it.polito.wa2.g27.server.ticketHistory

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketHistoryRepository: JpaRepository<TicketHistory, Int> {
}
package it.polito.wa2.g27.server.messages

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository: JpaRepository<Message, Int> {
    fun findAllByTicketId(ticketId: Int): List<Message>
}
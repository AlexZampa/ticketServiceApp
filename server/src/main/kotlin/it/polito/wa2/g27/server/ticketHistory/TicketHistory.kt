package it.polito.wa2.g27.server.ticketHistory

import it.polito.wa2.g27.server.ticket.Ticket
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "ticketHistory")
class TicketHistory {
    @Id @GeneratedValue
    var id: Int = 0
    @ManyToOne(optional = false)
    var ticket: Ticket? = null
    var status: String = ""
    var date: LocalDateTime = LocalDateTime.now()
}
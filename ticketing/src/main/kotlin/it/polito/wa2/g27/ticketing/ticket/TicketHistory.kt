package it.polito.wa2.g27.ticketing.ticket

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
    var ticket: Ticket = Ticket()
    var status: String = ""
    var date: LocalDateTime = LocalDateTime.now()
}
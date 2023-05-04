package it.polito.wa2.g27.server.ticketHistory

import it.polito.wa2.g27.server.ticket.Ticket
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime

@Entity
@Table(name = "ticketHistory")
class TicketHistory {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0
    @ManyToOne(optional = false)
    var ticket: Ticket? = null
    var status: String = ""
    var date: LocalDateTime = LocalDateTime.now()
}
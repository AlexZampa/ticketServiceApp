package it.polito.wa2.g27.server.messages

import it.polito.wa2.g27.server.profiles.Profile
import it.polito.wa2.g27.server.ticket.Ticket
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
class Message {
    @Id @GeneratedValue
    var id : Int = 0
    @ManyToOne(optional = false)
    var ticket: Ticket? = null
    @ManyToOne(optional = false)
    var sender: Profile? = null
    @ManyToOne(optional = false)
    var receiver: Profile? = null
    var text: String = ""
    var datetime: LocalDateTime = LocalDateTime.now()
}
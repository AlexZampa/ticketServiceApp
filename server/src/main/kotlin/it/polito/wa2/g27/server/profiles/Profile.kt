package it.polito.wa2.g27.server.profiles

import it.polito.wa2.g27.server.messages.Message
import it.polito.wa2.g27.server.ticket.Ticket
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "profiles")
class Profile {
    @Id
    var email: String = ""
    var username: String = ""
    var name: String = ""
    var surname: String = ""
    var dateofbirth: LocalDate = LocalDate.now()
    var hash: String = ""
    @OneToMany(mappedBy = "profile")
    var tickets: MutableSet<Ticket> = mutableSetOf()
    @OneToMany(mappedBy = "sender")
    var messagesSent: MutableSet<Message> = mutableSetOf()
    @OneToMany(mappedBy = "receiver")
    var messagesReceived: MutableSet<Message> = mutableSetOf()

    fun addTicket(t: Ticket) {
        t.profile = this
        tickets.add(t)
    }

    fun addMessageSent(m: Message) {
        m.sender = this
        messagesSent.add(m)
    }

    fun addMessageReceived(m: Message) {
        m.receiver = this
        messagesReceived.add(m)
    }

}
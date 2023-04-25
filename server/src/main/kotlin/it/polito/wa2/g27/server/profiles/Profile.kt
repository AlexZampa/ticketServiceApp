package it.polito.wa2.g27.server.profiles

import it.polito.wa2.g27.server.messages.Message
import it.polito.wa2.g27.server.ticket.Ticket
import jakarta.persistence.*
import java.security.MessageDigest
import java.time.LocalDate

@Entity
@Table(name = "profiles")
class Profile {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0
    var email: String = ""
    var username: String = ""
    var name: String = ""
    var surname: String = ""
    var dateofbirth: LocalDate = LocalDate.now()
    var hash: String = ""
    @OneToMany(mappedBy = "profile")
    var ticketsCreated: MutableSet<Ticket> = mutableSetOf()
    @OneToMany(mappedBy = "expert")
    var ticketsAssigned: MutableSet<Ticket> = mutableSetOf()
    @OneToMany(mappedBy = "sender")
    var messagesSent: MutableSet<Message> = mutableSetOf()
    @OneToMany(mappedBy = "receiver")
    var messagesReceived: MutableSet<Message> = mutableSetOf()

    fun addTicketCreated(t: Ticket) {
        t.profile = this
        ticketsCreated.add(t)
    }

    fun addTicketAssigned(t: Ticket) {
        t.expert = this
        ticketsAssigned.add(t)
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

fun ProfileDTO.toProfile(): Profile{
    val p = Profile()
    p.email = email
    p.username = username
    p.name = name
    p.surname = surname
    p.dateofbirth = LocalDate.parse(dateOfBirth)
    val digest = MessageDigest.getInstance("SHA-256").digest(hash.toByteArray())
    p.hash = digest.fold("") { str, it -> str + "%02x".format(it) }
    return p
}
package it.polito.wa2.g27.server.messages

import it.polito.wa2.g27.server.messages.attachments.Attachment
import it.polito.wa2.g27.server.profiles.Profile
import it.polito.wa2.g27.server.ticket.Ticket
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "messages")
class Message {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    var id : Int = 0
    @ManyToOne(optional = false)
    var ticket: Ticket? = null
    @ManyToOne(optional = false)
    var sender: Profile? = null
    @ManyToOne(optional = false)
    var receiver: Profile? = null
    var text: String = ""
    var datetime: LocalDateTime = LocalDateTime.now()
    @OneToMany(mappedBy = "message")
    var attachments: MutableSet<Attachment> = mutableSetOf()

    fun addAttachment(a: Attachment) {
        a.message = this
        attachments.add(a)
    }
}

fun MessageDTO.toMessage(): Message {
    val m = Message()
    m.id = id
    m.text = text
    m.datetime = dateTime

    return m
}
package it.polito.wa2.g27.server.ticket

import it.polito.wa2.g27.server.messages.Message
import it.polito.wa2.g27.server.products.Product
import it.polito.wa2.g27.server.profiles.Profile
import it.polito.wa2.g27.server.ticketHistory.TicketHistory
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "tickets")
class Ticket {
    @Id @GeneratedValue
    var id : Int = 0
    @ManyToOne(optional = false)
    var product : Product? = null
    var category: String = ""
    var priority: Int = 0
    var description: String = ""
    @ManyToOne(optional = false)
    var profile: Profile? = null
    @ManyToOne(optional = true)
    var expert: Profile? = null
    var chatId: String = ""
    @OneToMany(mappedBy = "ticket")
    var ticketHistory: MutableSet<TicketHistory> = mutableSetOf()
    @OneToMany(mappedBy = "ticket")
    var messages: MutableSet<Message> = mutableSetOf()

    fun addTicketHistory(th: TicketHistory) {
        th.ticket = this
        ticketHistory.add(th)
    }

    fun addMessage(m: Message) {
        m.ticket = this
        messages.add(m)
    }
}

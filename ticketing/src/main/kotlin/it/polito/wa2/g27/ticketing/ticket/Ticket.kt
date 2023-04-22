package it.polito.wa2.g27.ticketing.ticket

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "ticket")
class Ticket {
    @Id @GeneratedValue
    var id : Int = 0
    var productId : String = ""
    var category: String = ""
    var priority: Int = 0
    var description: String = ""
    var userId: String = ""
    var expertId: String = ""
    var chatId: String = ""
    @OneToMany(mappedBy = "id")
    var ticketHistory: List<TicketHistory> = emptyList()
}

package it.polito.wa2.g27.server.ticket

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class TicketController(private val ticketService: TicketService) {

    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/ticket/{id}")
    fun getProduct(@PathVariable id: Int) : TicketDTO? {
        return ticketService.getSingleTicket(id)
    }

}
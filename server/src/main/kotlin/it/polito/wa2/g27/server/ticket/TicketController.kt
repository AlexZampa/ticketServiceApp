package it.polito.wa2.g27.server.ticket

import it.polito.wa2.g27.server.exceptions.TicketBodyException
import it.polito.wa2.g27.server.products.ProductService
import it.polito.wa2.g27.server.profiles.ProfileDTO
import it.polito.wa2.g27.server.profiles.ProfileService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class TicketController(private val ticketService: TicketService,
                       private val profileService: ProfileService,
                       private val productService: ProductService) {

    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/tickets/{id}")
    fun getTicket(@PathVariable id: Int) : TicketDTO? {
        return ticketService.getSingleTicket(id)
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/tickets/open")
    fun getOpenTickets() : List<TicketDTO> {
        return ticketService.getOpenTickets()
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/tickets/created/{email}")
    fun getTicketsByProfile(@PathVariable email: String) : List<TicketDTO> {
        val profileDTO: ProfileDTO = profileService.getByEmail(email)
        return ticketService.getTicketsByProfile(profileDTO)
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/tickets/assigned/{email}")
    fun getAssignedTickets(@PathVariable email: String) : List<TicketDTO> {
        val profileDTO: ProfileDTO = profileService.getByEmail(email)
        return ticketService.getAssignedTickets(profileDTO)
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PostMapping("/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    fun postCreateTicket(@Valid @RequestBody ticketDTO: TicketDTO) {
        ticketService.createTicket(ticketDTO)
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PutMapping("/tickets/{id}/priority/{priority}")
    @ResponseStatus(HttpStatus.CREATED)
    fun putModifyPriority(@PathVariable id: Int, @PathVariable priority: Int) {
        ticketService.modifyPriority(id, priority)
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PutMapping("/tickets/{id}/expert")
    @ResponseStatus(HttpStatus.CREATED)
    fun putAssignExpert(@PathVariable id: Int, @RequestBody body: Map<String, Any>) {
        if(!body.containsKey("email") || !body.containsKey("priority") || (body["email"] !is String) || (body["priority"] !is Int))
            throw TicketBodyException("Invalid body format")
        ticketService.assignExpert(id, body["email"].toString(), body["priority"].toString().toInt())
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PutMapping("/tickets/{id}/stop")
    @ResponseStatus(HttpStatus.CREATED)
    fun putStopTicket(@PathVariable id: Int) {
        ticketService.stopTicketProgress(id)
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PutMapping("/tickets/{id}/close")
    @ResponseStatus(HttpStatus.CREATED)
    fun putCloseTicket(@PathVariable id: Int) {
        ticketService.closeTicket(id)
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PutMapping("/tickets/{id}/resolve")
    @ResponseStatus(HttpStatus.CREATED)
    fun putResolveTicket(@PathVariable id: Int) {
        ticketService.resolveTicketIssue(id)
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PutMapping("/tickets/{id}/reopen")
    @ResponseStatus(HttpStatus.CREATED)
    fun putReopenTicket(@PathVariable id: Int) {
        ticketService.reopenTicket(id)
    }

}



package it.polito.wa2.g27.server.ticket

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
    fun postModifyPriority(@PathVariable id: Int, @PathVariable priority: Int) {
        ticketService.modifyPriority(id, priority)
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PutMapping("/tickets/{id}/status/{status}")
    @ResponseStatus(HttpStatus.CREATED)
    fun postModifyStatus(@PathVariable id: Int, @PathVariable status: String) {
        ticketService.modifyStatus(id, status.uppercase())
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PutMapping("/tickets/{id}/expert")
    @ResponseStatus(HttpStatus.CREATED)
    fun postModifyExpert(@PathVariable id: Int, @RequestBody email: String) {
        ticketService.assignExpert(id, email)
    }

}
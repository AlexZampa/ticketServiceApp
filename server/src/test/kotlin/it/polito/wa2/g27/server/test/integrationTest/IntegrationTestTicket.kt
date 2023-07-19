package it.polito.wa2.g27.server.test.integrationTest

import it.polito.wa2.g27.server.products.ProductDTO
import it.polito.wa2.g27.server.products.ProductRepository
import it.polito.wa2.g27.server.products.toProduct
import it.polito.wa2.g27.server.profiles.ProfileDTO
import it.polito.wa2.g27.server.profiles.ProfileRepository
import it.polito.wa2.g27.server.profiles.ProfileService
import it.polito.wa2.g27.server.profiles.toProfile
import it.polito.wa2.g27.server.ticket.TicketDTO
import it.polito.wa2.g27.server.ticket.TicketRepository
import it.polito.wa2.g27.server.ticket.TicketService
import it.polito.wa2.g27.server.ticketHistory.TicketHistoryRepository
import it.polito.wa2.g27.server.ticketHistory.TicketStatus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IntegrationTestTicket {

    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:latest")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.jpa.hibernate.ddl-auto") { "create-drop" }
        }

    }
    @LocalServerPort
    protected var port: Int = 0

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var ticketService: TicketService
    @Autowired
    lateinit var profileService: ProfileService
    @Autowired
    private lateinit var profileRepository: ProfileRepository
    @Autowired
    private lateinit var ticketRepository: TicketRepository
    @Autowired
    private lateinit var productRepository: ProductRepository
    @Autowired
    private lateinit var ticketHistoryRepository: TicketHistoryRepository

    var user1 = ProfileDTO(1, "user1@mail.com", "user1", "Frank", "Matano", "1989-09-14" , "client")
    var user2 = ProfileDTO(2, "user2@mail.com", "user2", "Marco", "Bay", "1999-10-02", "client" )

    var prod1 = ProductDTO("A01", "Lego Star Wars", "LEGO", "Space")
    var prod2 = ProductDTO("B01", "Lego Duplo Fashion Blogger", "LEGO Duplo", "Fashion")

    var ticket1 = TicketDTO(0, "A01", "Lego", 0, "Non funziona", 1, null, null)
    var ticket2 = TicketDTO(0, "B01", "Duplo", 0, "NO istruzioni", 2, null, null)

    @BeforeEach
    fun populateDB() {
        val profile1 = user1.toProfile()
        val profile2 = user2.toProfile()
        val product1 = prod1.toProduct()
        val product2 = prod2.toProduct()

        profileRepository.save(profile1)
        profileRepository.save(profile2)

        productRepository.save(product1)
        productRepository.save(product2)

        ticketHistoryRepository.deleteAll()
        ticketRepository.deleteAll()
    }

    @Test
    fun getTicketCorrect() {
        val ticketId = ticketService.createTicket(ticket1).id
        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port//tickets/${ticketId}",
            HttpMethod.GET,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun getTicketNoId() {
        val ticketId = ticketService.createTicket(ticket1).id
        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/tickets/500",
            HttpMethod.GET,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun getOpenTickets() {
        val newTicket1 = ticketService.createTicket(ticket1)
        val newTicket2 = ticketService.createTicket(ticket2)
        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/tickets/open",
            HttpMethod.GET,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun getTicketsByProfile() {
        val newTicket1 = ticketService.createTicket(ticket2)
        val newTicket2 = ticketService.createTicket(ticket2)
        val profile = profileService.getById(newTicket1.profileId!!)
        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/tickets/created/${profile.email}",
            HttpMethod.GET,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun getAssignedTickets() {
        var newTicket1 = ticketService.createTicket(ticket1)
        ticketService.assignExpert(newTicket1.id, user1.email, 2)

        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/tickets/assigned/${user1.email}",
            HttpMethod.GET,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun postCreateTicket() {
        val ticketDTO = TicketDTO(0, prod1.id, "Lego", 1, "Manca un pezzo", user1.id, null, TicketStatus.OPEN)

        val headers = HttpHeaders()
        val requestEntity= HttpEntity<TicketDTO>(ticketDTO, headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/tickets",
            HttpMethod.POST,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
    }

    @Test
    fun putModifyPriority() {
        val newTicket1 = ticketService.createTicket(ticket1)

        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/tickets/${newTicket1.id}/priority/2",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
    }

    @Test
    fun putAssignExpert() {
        val newTicket1 = ticketService.createTicket(ticket1)
        val profile = profileService.getById(newTicket1.profileId!!)

        data class Body (
            val email: String,
            val priority: Int
        )
        val headers = HttpHeaders()
        var body = Body(profile.email, 3)
        val requestEntity= HttpEntity<Body>(body, headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/tickets/${newTicket1.id}/expert",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)

    }

    @Test
    fun putStopTicket() {
        val newTicket1 = ticketService.createTicket(ticket1)
        ticketService.assignExpert(newTicket1.id, user1.email, 2)

        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/tickets/${newTicket1.id}/stop",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)

    }

    @Test
    fun putCloseTicket() {
        val newTicket1 = ticketService.createTicket(ticket1)

        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/tickets/${newTicket1.id}/close",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
    }

    @Test
    fun putResolveTicket() {
        val newTicket1 = ticketService.createTicket(ticket1)

        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/tickets/${newTicket1.id}/resolve",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
    }

    @Test
    fun putReopenTicket() {
        val newTicket1 = ticketService.createTicket(ticket1)
        ticketService.closeTicket(newTicket1.id)

        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/tickets/${newTicket1.id}/reopen",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java
        )

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
    }

    @Test
    fun putModifyPriorityNotValid() {
        val newTicket1 = ticketService.createTicket(ticket1)

        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response1 = restTemplate.exchange(
            "http://localhost:$port/tickets/${newTicket1.id}/priority/0",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response1.statusCode)

        val response2 = restTemplate.exchange(
            "http://localhost:$port/tickets/${newTicket1.id}/priority/4",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response2.statusCode)

    }

    @Test
    fun putTicketNotInProgress() {
        val newTicket1 = ticketService.createTicket(ticket1)

        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/tickets/${newTicket1.id}/stop",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)

    }

    @Test
    fun putTicketAlreadyClosed() {
        val newTicket1 = ticketService.createTicket(ticket1)
        ticketService.closeTicket(newTicket1.id)

        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/tickets/${newTicket1.id}/close",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun putTicketIsClosed() {
        val newTicket1 = ticketService.createTicket(ticket1)
        ticketService.closeTicket(newTicket1.id)

        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/tickets/${newTicket1.id}/resolve",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun putTicketIsNotClosedOrResolved() {
        val newTicket1 = ticketService.createTicket(ticket1)

        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/tickets/${newTicket1.id}/reopen",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

}
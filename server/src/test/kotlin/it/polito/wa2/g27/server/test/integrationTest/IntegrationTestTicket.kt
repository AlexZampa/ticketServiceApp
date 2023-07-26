package it.polito.wa2.g27.server.test.integrationTest

import it.polito.wa2.g27.server.products.ProductDTO
import it.polito.wa2.g27.server.products.ProductRepository
import it.polito.wa2.g27.server.products.toProduct
import it.polito.wa2.g27.server.profiles.ProfileDTO
import it.polito.wa2.g27.server.profiles.ProfileRepository
import it.polito.wa2.g27.server.profiles.ProfileService
import it.polito.wa2.g27.server.security.AuthDTO
import it.polito.wa2.g27.server.security.AuthService
import it.polito.wa2.g27.server.ticket.TicketDTO
import it.polito.wa2.g27.server.ticket.TicketRepository
import it.polito.wa2.g27.server.ticket.TicketService
import it.polito.wa2.g27.server.ticketHistory.TicketHistoryRepository
import it.polito.wa2.g27.server.ticketHistory.TicketStatus
import org.junit.jupiter.api.AfterEach
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
    @Autowired
    lateinit var authService: AuthService

    val user1 = ProfileDTO(1, "usertest1@mail.com", "usertest1", "Frank", "Matano", "1989-09-14" , "client", "password")
    val user2 = ProfileDTO(2, "usertest2@mail.com", "usertest2", "Marco", "Bay", "1999-10-02" , "manager", "password")
    val user3 = ProfileDTO(3, "usertest3@mail.com", "usertest3", "Mary", "White", "1992-08-02", "expert", "password")

    var authenticatedProfile1: ProfileDTO? = null
    var authenticatedProfile2: ProfileDTO? = null
    var authenticatedProfile3: ProfileDTO? = null

    var prod1 = ProductDTO("A01", "Lego Star Wars", "LEGO", "Space")
    var prod2 = ProductDTO("B01", "Lego Duplo Fashion Blogger", "LEGO Duplo", "Fashion")

    //var ticket1 = TicketDTO(0, "A01", "Lego", 0, "Non funziona", null, null, null)
    //var ticket2 = TicketDTO(0, "B01", "Duplo", 0, "NO istruzioni", null, null, null)

    @BeforeEach
    fun populateDB() {
        authenticatedProfile1 = authService.signup(user1)
        authenticatedProfile2 = authService.createManager(user2)
        authenticatedProfile3 = authService.createExpert(user3)
        authenticatedProfile1 = authService.login(AuthDTO(user1.email, user1.password!!))
        authenticatedProfile2 = authService.login(AuthDTO(user2.email, user2.password!!))
        authenticatedProfile3 = authService.login(AuthDTO(user3.email, user3.password!!))

        val product1 = prod1.toProduct()
        val product2 = prod2.toProduct()

        productRepository.save(product1)
        productRepository.save(product2)
    }

    @AfterEach
    fun deleteDB() {
        ticketHistoryRepository.deleteAll()
        ticketRepository.deleteAll()
        productRepository.deleteAll()

        authService.deleteProfile(user1.email)
        authService.deleteProfile(user2.email)
        authService.deleteProfile(user3.email)
    }

    @Test
    fun getTicketTest() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val ticketId = ticketService.createTicket(ticket1).id
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile1?.token )
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/authenticated/tickets/${ticketId}",
            HttpMethod.GET,
            requestEntity,
            TicketDTO::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertEquals(response.body?.status, TicketStatus.OPEN )
        Assertions.assertTrue { response.body?.profileId == authenticatedProfile1?.id }
        Assertions.assertTrue { response.body?.productId == prod1.id }
        Assertions.assertTrue { response.body?.priority == 0 }
    }

    @Test
    fun getTicketNotFoundException() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        ticketService.createTicket(ticket1).id
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile1?.token )
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/authenticated/tickets/500",
            HttpMethod.GET,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun getOpenTickets() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val ticket2 = TicketDTO(0, prod2.id, "Duplo", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        ticketService.createTicket(ticket1)
        ticketService.createTicket(ticket2)
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile2?.token )
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/manager/tickets/open",
            HttpMethod.GET,
            requestEntity,
            Any::class.java)
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun getOpenTicketsForbidden() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val ticket2 = TicketDTO(0, prod2.id, "Duplo", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        ticketService.createTicket(ticket1)
        ticketService.createTicket(ticket2)
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile3?.token )
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/manager/tickets/open",
            HttpMethod.GET,
            requestEntity,
            Any::class.java)
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.statusCode)
    }

    @Test
    fun getTicketsByProfile() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        ticketService.createTicket(ticket1)
        ticketService.createTicket(ticket1)
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile1?.token )
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/client/tickets/created/${authenticatedProfile1?.email}",
            HttpMethod.GET,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun getAssignedTickets() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        var newTicket1 = ticketService.createTicket(ticket1)
        ticketService.assignExpert(newTicket1.id, authenticatedProfile3?.email!!, 2)

        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        headers.set("Authorization", "Bearer " + authenticatedProfile3?.token )
        val response = restTemplate.exchange(
            "http://localhost:$port/expert/tickets/assigned/${user1.email}",
            HttpMethod.GET,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun postCreateTicket() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile1?.token )
        val requestEntity= HttpEntity<TicketDTO>(ticket1, headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/client/tickets",
            HttpMethod.POST,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
    }

    @Test
    fun putModifyPriority() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile2?.token )
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/manager/tickets/${newTicket1.id}/priority/2",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
    }

    @Test
    fun putAssignExpert() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)
        data class Body (
            val email: String,
            val priority: Int
        )
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile2?.token )
        val body = Body(authenticatedProfile3?.email!!, 3)
        val requestEntity= HttpEntity<Body>(body, headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/manager/tickets/${newTicket1.id}/expert",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)
        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
    }

    @Test
    fun putAssignExpertInvalidBodyError() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)

        data class Body (
            val emailError: String,
            val priority: Int
        )
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile2?.token )
        val body = Body(authenticatedProfile3?.email!!, 3)
        val requestEntity= HttpEntity<Body>(body, headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/manager/tickets/${newTicket1.id}/expert",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun putAssignExpertPriorityInvalidError() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)

        data class Body (
            val email: String,
            val priority: Int
        )
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile2?.token )
        val body = Body(authenticatedProfile3?.email!!, 20)
        val requestEntity= HttpEntity<Body>(body, headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/manager/tickets/${newTicket1.id}/expert",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun putStopTicket() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)
        ticketService.assignExpert(newTicket1.id, authenticatedProfile3?.email!!, 2)

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile2?.token )
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/manager/tickets/${newTicket1.id}/stop",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        val ticket = ticketService.getSingleTicket(newTicket1.id)

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
        Assertions.assertEquals(ticket.status, TicketStatus.OPEN)
    }

    @Test
    fun putCloseTicket() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile1?.token )
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/authenticated/tickets/${newTicket1.id}/close",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)
        val ticket = ticketService.getSingleTicket(newTicket1.id)

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
        Assertions.assertEquals(ticket.status, TicketStatus.CLOSED)
    }

    @Test
    fun putResolveTicket() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile3?.token )
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/expert/tickets/${newTicket1.id}/resolve",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        val ticket = ticketService.getSingleTicket(newTicket1.id)

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
        Assertions.assertEquals(ticket.status, TicketStatus.RESOLVED)
    }

    @Test
    fun putReopenTicket() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)
        ticketService.closeTicket(newTicket1.id)

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile1?.token )
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/client/tickets/${newTicket1.id}/reopen",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java
        )
        val ticket = ticketService.getSingleTicket(newTicket1.id)

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
        Assertions.assertEquals(ticket.status, TicketStatus.REOPENED)
    }

    @Test
    fun putModifyPriorityNotValidError() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile2?.token )
        val requestEntity= HttpEntity<Unit>(headers)

        val response1 = restTemplate.exchange(
            "http://localhost:$port/manager/tickets/${newTicket1.id}/priority/0",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response1.statusCode)

        val response2 = restTemplate.exchange(
            "http://localhost:$port/manager/tickets/${newTicket1.id}/priority/4",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response2.statusCode)

    }

    @Test
    fun putStopTicketNotInProgressError() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile2?.token )
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/manager/tickets/${newTicket1.id}/stop",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun putCloseTicketAlreadyClosedError() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)
        ticketService.closeTicket(newTicket1.id)

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile1?.token )
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/authenticated/tickets/${newTicket1.id}/close",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun putResolveTicketIsClosedError() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)
        ticketService.closeTicket(newTicket1.id)

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile3?.token )
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/expert/tickets/${newTicket1.id}/resolve",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun putReopenTicketIsNotClosedOrResolvedError() {
        val ticket1 = TicketDTO(0, prod1.id, "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile1?.token )
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/client/tickets/${newTicket1.id}/reopen",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

}
package it.polito.wa2.g27.server.test.serviceTest

import it.polito.wa2.g27.server.exceptions.TicketNotFoundException
import it.polito.wa2.g27.server.exceptions.TicketPriorityNotValidException
import it.polito.wa2.g27.server.exceptions.TicketStatusException
import it.polito.wa2.g27.server.products.ProductDTO
import it.polito.wa2.g27.server.products.ProductRepository
import it.polito.wa2.g27.server.products.toProduct
import it.polito.wa2.g27.server.profiles.ProfileDTO
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
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class TicketTests {
    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:latest")
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.jpa.hibernate.ddl-auto") {"create-drop"}
        }
    }
    @LocalServerPort
    protected var port: Int = 0
    @Autowired
    lateinit var ticketService: TicketService
    @Autowired
    lateinit var profileService: ProfileService
    @Autowired
    lateinit var authService: AuthService
    @Autowired
    private lateinit var ticketRepository: TicketRepository
    @Autowired
    private lateinit var productRepository: ProductRepository
    @Autowired
    private lateinit var ticketHistoryRepository: TicketHistoryRepository


    val user1 = ProfileDTO(1, "usertest1@mail.com", "usertest1", "Frank", "Matano", "1989-09-14" , "client", "password")
    val user2 = ProfileDTO(2, "usertest2@mail.com", "usertest2", "Marco", "Bay", "1999-10-02" , "expert", "password")
    val user3 = ProfileDTO(3, "usertest3@mail.com", "usertest3", "Lorenzo", "Biondo", "1999-10-02", "manager", "password")

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
        authenticatedProfile2 = authService.signup(user2)
        authenticatedProfile3 = authService.signup(user3)
        authenticatedProfile1 = authService.login(AuthDTO(user1.email, user1.password!!))
        authenticatedProfile2 = authService.login(AuthDTO(user2.email, user2.password!!))
        authenticatedProfile3 = authService.login(AuthDTO(user3.email, user3.password!!))
        println(authenticatedProfile1)

        val product1 = prod1.toProduct()
        val product2 = prod2.toProduct()

        productRepository.save(product1)
        productRepository.save(product2)

    }

    @AfterEach
    fun deleteDB() {
        ticketHistoryRepository.deleteAll()
        ticketRepository.deleteAll()

        authService.deleteProfile(user1.email)
        authService.deleteProfile(user2.email)
        authService.deleteProfile(user3.email)
    }

    @Test
    fun getTicket() {
        val ticket1 = TicketDTO(0, "A01", "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket = ticketService.createTicket(ticket1)
        val ticketDTO = ticketService.getSingleTicket(newTicket.id)
        Assertions.assertEquals(newTicket, ticketDTO)
    }

    @Test
    fun getOpenTickets() {
        val ticket1 = TicketDTO(0, "A01", "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val ticket2 = TicketDTO(0, "B01", "Duplo", 0, "NO istruzioni", authenticatedProfile2?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)
        val newTicket2 = ticketService.createTicket(ticket2)
        val openTickets = ticketService.getOpenTickets().sortedBy { it.id }
        val expectedTickets = listOf(newTicket1, newTicket2).sortedBy { it.id }
        Assertions.assertIterableEquals(expectedTickets, openTickets)
    }

    @Test
    fun getTicketsByProfile() {
        val ticket2 = TicketDTO(0, "B01", "Duplo", 0, "NO istruzioni", authenticatedProfile2?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket2)
        val newTicket2 = ticketService.createTicket(ticket2)
        val profile = profileService.getById(newTicket1.profileId!!)
        val ticketList = ticketService.getTicketsByProfile(profile).sortedBy { it.id }
        val expectedTickets = listOf(newTicket1, newTicket2).sortedBy { it.id }
        Assertions.assertIterableEquals(expectedTickets, ticketList)
    }

    @Test
    fun getAssignedTickets() {
        val ticket1 = TicketDTO(0, "A01", "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        var newTicket1 = ticketService.createTicket(ticket1)
        ticketService.assignExpert(newTicket1.id, user1.email, 2)
        newTicket1 = ticketService.getSingleTicket(newTicket1.id)
        val ticketList = ticketService.getAssignedTickets(authenticatedProfile1!!).sortedBy { it.id }
        val expectedTickets = listOf(newTicket1).sortedBy { it.id }
        Assertions.assertIterableEquals(expectedTickets, ticketList)
    }

    @Test
    fun postCreateTicket() {
        val ticketDTO = TicketDTO(0, prod1.id, "Lego", 1, "Manca un pezzo",
            authenticatedProfile1?.id, null, TicketStatus.OPEN)
        val expectedTicketDTO = ticketService.createTicket(ticketDTO)
        val newTicketDTO = ticketService.getSingleTicket(expectedTicketDTO.id)
        Assertions.assertEquals(expectedTicketDTO, newTicketDTO)
    }

    @Test
    fun putModifyPriority() {
        val ticket1 = TicketDTO(0, "A01", "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)
        ticketService.modifyPriority(newTicket1.id, 2)
        val modifiedTicket = ticketService.getSingleTicket(newTicket1.id)
        Assertions.assertEquals(2, modifiedTicket.priority)
    }

    @Test
    fun putAssignExpert() {
        val ticket1 = TicketDTO(0, "A01", "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)
        val profile = profileService.getById(newTicket1.profileId!!)
        ticketService.assignExpert(newTicket1.id, profile.email, 3)
        val modifiedTicket = ticketService.getSingleTicket(newTicket1.id)
        Assertions.assertEquals(profile.id, modifiedTicket.expertId)
        Assertions.assertEquals(TicketStatus.PROGRESS, modifiedTicket.status)
    }

    @Test
    fun putStopTicket() {
        val ticket1 = TicketDTO(0, "A01", "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)
        ticketService.assignExpert(newTicket1.id, user1.email, 2)
        ticketService.stopTicketProgress(newTicket1.id)
        val modifiedTicket = ticketService.getSingleTicket(newTicket1.id)
        Assertions.assertEquals(TicketStatus.OPEN, modifiedTicket.status)
        Assertions.assertEquals(null, modifiedTicket.expertId)
        Assertions.assertEquals(0, modifiedTicket.priority)

    }

    @Test
    fun putCloseTicket() {
        val ticket1 = TicketDTO(0, "A01", "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)
        ticketService.closeTicket(newTicket1.id)
        val modifiedTicket = ticketService.getSingleTicket(newTicket1.id)
        Assertions.assertEquals(TicketStatus.CLOSED, modifiedTicket.status)
        Assertions.assertEquals(null, modifiedTicket.expertId)
        Assertions.assertEquals(0, modifiedTicket.priority)
    }

    @Test
    fun putResolveTicket() {
        val ticket1 = TicketDTO(0, "A01", "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)
        ticketService.resolveTicketIssue(newTicket1.id)
        val modifiedTicket = ticketService.getSingleTicket(newTicket1.id)
        Assertions.assertEquals(TicketStatus.RESOLVED, modifiedTicket.status)
        Assertions.assertEquals(null, modifiedTicket.expertId)
        Assertions.assertEquals(0, modifiedTicket.priority)
    }

    @Test
    fun putReopenTicket() {
        val ticket1 = TicketDTO(0, "A01", "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)
        ticketService.closeTicket(newTicket1.id)
        ticketService.reopenTicket(newTicket1.id)
        val modifiedTicket = ticketService.getSingleTicket(newTicket1.id)
        Assertions.assertEquals(TicketStatus.REOPENED, modifiedTicket.status)
        Assertions.assertEquals(null, modifiedTicket.expertId)
        Assertions.assertEquals(0, modifiedTicket.priority)
    }

    @Test
    fun getTicketNotExist() {
        Assertions.assertThrows(TicketNotFoundException::class.java, {
            ticketService.getSingleTicket(1)
        }, "Ticket Not Found")
    }

    @Test
    fun putModifyPriorityNotValid() {
        val ticket1 = TicketDTO(0, "A01", "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)

        Assertions.assertThrows(TicketPriorityNotValidException::class.java, {
            ticketService.modifyPriority(newTicket1.id, 0)
        }, "Priority Invalid")

        Assertions.assertThrows(TicketPriorityNotValidException::class.java, {
            ticketService.modifyPriority(newTicket1.id, 4)
        }, "Priority Invalid")

    }

    @Test
    fun putTicketNotInProgress() {
        val ticket1 = TicketDTO(0, "A01", "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)
        //ticketService.assignExpert(newTicket1.id, user1.email, 2)
        Assertions.assertThrows(TicketStatusException::class.java, {
            ticketService.stopTicketProgress(newTicket1.id)
        }, "Ticket not in Progress")

    }

    @Test
    fun putTicketAlreadyClosed() {
        val ticket1 = TicketDTO(0, "A01", "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)
        ticketService.closeTicket(newTicket1.id)
        Assertions.assertThrows(TicketStatusException::class.java, {
            ticketService.closeTicket(newTicket1.id)
        }, "Ticket already closed")
    }

    @Test
    fun putTicketIsClosed() {
        val ticket1 = TicketDTO(0, "A01", "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)
        ticketService.closeTicket(newTicket1.id)

        Assertions.assertThrows(TicketStatusException::class.java, {
            ticketService.resolveTicketIssue(newTicket1.id)
        }, "Ticket is Closed")
    }

    @Test
    fun putTicketIsNotClosedOrResolved() {
        val ticket1 = TicketDTO(0, "A01", "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        val newTicket1 = ticketService.createTicket(ticket1)

        Assertions.assertThrows(TicketStatusException::class.java, {
            ticketService.reopenTicket(newTicket1.id)
        }, "The Ticket is not Closed or Resolved")
    }

}

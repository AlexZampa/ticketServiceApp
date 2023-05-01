package it.polito.wa2.g27.server.integrationTests;

import it.polito.wa2.g27.server.exceptions.TicketBodyException
import it.polito.wa2.g27.server.products.Product
import it.polito.wa2.g27.server.products.ProductDTO
import it.polito.wa2.g27.server.products.ProductRepository
import it.polito.wa2.g27.server.profiles.Profile
import it.polito.wa2.g27.server.profiles.ProfileDTO
import it.polito.wa2.g27.server.profiles.ProfileRepository
import it.polito.wa2.g27.server.profiles.ProfileService
import it.polito.wa2.g27.server.ticket.Ticket
import it.polito.wa2.g27.server.ticket.TicketDTO
import it.polito.wa2.g27.server.ticket.TicketRepository
import it.polito.wa2.g27.server.ticket.TicketService
import it.polito.wa2.g27.server.ticketHistory.TicketStatus
import jakarta.validation.Valid
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.web.bind.annotation.*
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.sql.DriverManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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
    private lateinit var profileRepository: ProfileRepository
    @Autowired
    private lateinit var ticketRepository: TicketRepository
    @Autowired
    private lateinit var productRepository: ProductRepository

    val user1 = ProfileDTO(1, "user1@mail.com", "user1", "Frank", "Matano", "1989-09-14" )
    val user2 = ProfileDTO(2, "user2@mail.com", "user2", "Marco", "Bay", "1999-10-02" )

    val tick1 = TicketDTO(10, "A01", "Lego", 1, "Non funziona", 2, 1, TicketStatus.PROGRESS)
    val tick2 = TicketDTO(11, "B01", "Duplo", 1, "NO istruzioni", 1, null, TicketStatus.OPEN)

    val prod1 = ProductDTO("A01", "Lego Star Wars", "LEGO", "Space")
    val prod2 = ProductDTO("B01", "Lego Duplo Fashion Blogger", "LEGO Duplo", "Fashion")

    @BeforeEach
    fun populateDB() {
        val profile1 = Profile().apply {
            id = user1.id!!; email = user1.email
            name = user1.name; surname = user1.surname; username = user1.username
            dateofbirth = LocalDate.parse(user1.dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

        val profile2 = Profile().apply {
            id = user2.id!!; email = user2.email
            name = user2.name; surname = user2.surname; username = user2.username
            dateofbirth = LocalDate.parse(user2.dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

        val product1 = Product().apply {
            id = prod1.id; name = prod1.name;
            brand = prod1.brand; description = prod1.description;
        }

        val product2 = Product().apply {
            id = prod2.id; name = prod2.name;
            brand = prod2.brand; description = prod2.description;
        }

        val ticket1 = Ticket().apply {
            id = tick1.id; product = product1; category = tick1.category;
            priority = tick1.priority!!; description = tick1.description;
            profile = profile2; expert = profile1;

        }

        val ticket2 = Ticket().apply {
            id = tick2.id; product = product2; category = tick2.category;
            priority = tick2.priority!!; description = tick2.description;
            profile = profile1; expert = null;

        }

        ticketRepository.delete(ticket1);
        ticketRepository.delete(ticket2);

        productRepository.delete(product1);
        productRepository.delete(product2);

        profileRepository.delete(profile1)
        profileRepository.delete(profile2)

        profileRepository.save(profile1)
        profileRepository.save(profile2)

        productRepository.save(product1);
        productRepository.save(product2);

        ticketRepository.save(ticket1);
        ticketRepository.save(ticket2);
    }

    @Test
    fun getTicket() {
        val ticketDTO = ticketService.getSingleTicket(tick1.id)

        Assertions.assertEquals(tick1, ticketDTO)
    }

    @Test
    fun getOpenTickets() {
        val openTickets = ticketService.getOpenTickets()

        val expectedTickets = listOf(tick2)

        Assertions.assertEquals(expectedTickets, openTickets)
    }

    @Test
    fun getTicketsByProfile() {
        val ticketsDTO = ticketService.getTicketsByProfile(user1)
        println(ticketsDTO)
        val expectedTickets = listOf(tick2)

        Assertions.assertEquals(expectedTickets, ticketsDTO)
    }

    @Test
    fun getAssignedTickets() {
        val profileDTO: ProfileDTO = profileService.getByEmail(user1.email)
        val ticketDTO = ticketService.getAssignedTickets(profileDTO)

        val expectedTickets = listOf(
            tick1
        )

        Assertions.assertEquals(expectedTickets, ticketDTO)
    }

    @Test
    fun postCreateTicket() {
        val ticketDTO = TicketDTO(0, prod1.id, "Lego", 1, "Manca un pezzo", user1.id, null, TicketStatus.OPEN)

        val expectedTicketDTO = ticketService.createTicket(ticketDTO)
        val newTicketDTO = ticketService.getSingleTicket(expectedTicketDTO.id)

        Assertions.assertEquals(expectedTicketDTO, newTicketDTO)
    }

    @Test
    fun putModifyPriority() {
        ticketService.modifyPriority(tick1.id, 2)

        val ticketDTO = ticketService.getSingleTicket(tick1.id)

        println(ticketDTO?.status)

        Assertions.assertEquals(2, ticketDTO?.priority)
    }

    @Test
    fun putAssignExpert() {
        ticketService.assignExpert(tick2.id, user2.email, 3)
        val newTicketDTO = ticketService.getSingleTicket(tick2.id)

        Assertions.assertEquals(user2.id, newTicketDTO?.expertId)
        Assertions.assertEquals(TicketStatus.PROGRESS, newTicketDTO?.status)
    }

    @Test
    fun putStopTicket() {
        ticketService.stopTicketProgress(tick1.id)
        val newTicketDTO = ticketService.getSingleTicket(tick1.id)

        Assertions.assertEquals(TicketStatus.OPEN, newTicketDTO?.status)
        Assertions.assertEquals(null, newTicketDTO?.expertId)
        Assertions.assertEquals(0, newTicketDTO?.priority)

    }

    @Test
    fun putCloseTicket() {
        ticketService.closeTicket(tick1.id)
        val newTicketDTO = ticketService.getSingleTicket(tick1.id)

        Assertions.assertEquals(TicketStatus.CLOSED, newTicketDTO?.status)
        Assertions.assertEquals(null, newTicketDTO?.expertId)
        Assertions.assertEquals(0, newTicketDTO?.priority)
    }

    @Test
    fun putResolveTicket() {
        ticketService.resolveTicketIssue(tick1.id)
        val newTicketDTO = ticketService.getSingleTicket(tick1.id)

        Assertions.assertEquals(TicketStatus.RESOLVED, newTicketDTO?.status)
        Assertions.assertEquals(null, newTicketDTO?.expertId)
        Assertions.assertEquals(0, newTicketDTO?.priority)
    }

    @Test
    fun putReopenTicket() {
        ticketService.closeTicket(tick1.id)
        ticketService.reopenTicket(tick1.id)
        val newTicketDTO = ticketService.getSingleTicket(tick1.id)

        Assertions.assertEquals(TicketStatus.REOPENED, newTicketDTO?.status)
        Assertions.assertEquals(null, newTicketDTO?.expertId)
        Assertions.assertEquals(0, newTicketDTO?.priority)
    }

}

/*

//Write here your integration test
val testRestTemplate = TestRestTemplate()
val response: ResponseEntity<String> = testRestTemplate.getForEntity<String>(
    "http://localhost:8080/profiles/pollo2.matano@gmail.com",
    String
)

 */
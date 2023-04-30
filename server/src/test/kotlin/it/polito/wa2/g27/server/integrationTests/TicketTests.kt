package it.polito.wa2.g27.server.integrationTests;

import it.polito.wa2.g27.server.exceptions.TicketBodyException
import it.polito.wa2.g27.server.products.ProductDTO
import it.polito.wa2.g27.server.profiles.ProfileDTO
import it.polito.wa2.g27.server.profiles.ProfileService
import it.polito.wa2.g27.server.ticket.TicketDTO
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

    @BeforeEach
    fun populateDB() {
        DriverManager.getConnection(postgres.jdbcUrl, postgres.username, postgres.password).use { conn ->
            val script = File("src/test/resources/schema_populate.sql").readText()
            conn.createStatement().use { stmt ->
                stmt.execute(script)
            }
        }
    }

    @Test
    fun getTicket() {
        val ticketDTO = ticketService.getSingleTicket(10)
        
        val expectedTicketDTO = TicketDTO(10, "A01", "Lego", 1, "Non funziona", 32, 31, TicketStatus.PROGRESS)

        Assertions.assertEquals(expectedTicketDTO, ticketDTO)
    }

    @Test
    fun getOpenTickets() {
        val openTickets = ticketService.getOpenTickets()

        val expectedTickets = listOf(
            TicketDTO(11, "B01", "Duplo", 1, "NO istruzioni", 31, null, TicketStatus.OPEN)
        )

        Assertions.assertEquals(expectedTickets, openTickets)
    }

    @Test
    fun getTicketsByProfile() {
        val profileDTO: ProfileDTO = profileService.getByEmail("paul.dano@gmail.com")
        val ticketDTO = ticketService.getTicketsByProfile(profileDTO)

        val expectedTickets = listOf(
            TicketDTO(11, "B01", "Duplo", 1, "NO istruzioni", 31, null, TicketStatus.OPEN)
        )

        Assertions.assertEquals(expectedTickets, ticketDTO)
    }

    @Test
    fun getAssignedTickets() {
        val profileDTO: ProfileDTO = profileService.getByEmail("paul.dano@gmail.com")
        val ticketDTO = ticketService.getAssignedTickets(profileDTO)

        val expectedTickets = listOf(
            TicketDTO(10, "A01", "Lego", 1, "Non funziona", 32, 31, TicketStatus.PROGRESS)
        )

        Assertions.assertEquals(expectedTickets, ticketDTO)
    }

    @Test
    fun postCreateTicket() {
        val ticketDTO = TicketDTO(0, "A01", "Lego", 1, "Manca un pezzo", 32, null, TicketStatus.OPEN)

        val expectedTicketDTO = ticketService.createTicket(ticketDTO)
        val newTicketDTO = ticketService.getSingleTicket(expectedTicketDTO.id)

        Assertions.assertEquals(expectedTicketDTO, newTicketDTO)
    }

    @Test
    fun putModifyPriority() {
        ticketService.modifyPriority(10, 2)

        val ticketDTO = ticketService.getSingleTicket(10)

        println(ticketDTO?.status)

        Assertions.assertEquals(2, ticketDTO?.priority)
    }

    @Test
    fun putAssignExpert() {
        ticketService.assignExpert(11, "ciaone.matano@gmail.com", 3)
        val newTicketDTO = ticketService.getSingleTicket(11)

        Assertions.assertEquals(32, newTicketDTO?.expertId)
        Assertions.assertEquals(TicketStatus.PROGRESS, newTicketDTO?.status)
    }

    @Test
    fun putStopTicket() {
        ticketService.stopTicketProgress(10)
        val newTicketDTO = ticketService.getSingleTicket(10)

        Assertions.assertEquals(TicketStatus.OPEN, newTicketDTO?.status)
        Assertions.assertEquals(null, newTicketDTO?.expertId)
        Assertions.assertEquals(0, newTicketDTO?.priority)

        //ticketService.stopTicketProgress(id)
    }

    @Test
    fun putCloseTicket() {
        ticketService.closeTicket(10)
        val newTicketDTO = ticketService.getSingleTicket(10)

        Assertions.assertEquals(TicketStatus.CLOSED, newTicketDTO?.status)
        Assertions.assertEquals(null, newTicketDTO?.expertId)
        Assertions.assertEquals(0, newTicketDTO?.priority)
    }

    @Test
    fun putResolveTicket() {
        ticketService.resolveTicketIssue(10)
        val newTicketDTO = ticketService.getSingleTicket(10)

        Assertions.assertEquals(TicketStatus.RESOLVED, newTicketDTO?.status)
        Assertions.assertEquals(null, newTicketDTO?.expertId)
        Assertions.assertEquals(0, newTicketDTO?.priority)
    }

    @Test
    fun putReopenTicket() {
        putCloseTicket()
        ticketService.reopenTicket(10)
        val newTicketDTO = ticketService.getSingleTicket(10)

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
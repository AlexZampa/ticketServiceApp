package it.polito.wa2.g27.server.test.serviceTest

import it.polito.wa2.g27.server.messages.*
import it.polito.wa2.g27.server.messages.attachments.AttachmentRepository
import it.polito.wa2.g27.server.products.ProductDTO
import it.polito.wa2.g27.server.products.ProductRepository
import it.polito.wa2.g27.server.products.toProduct
import it.polito.wa2.g27.server.profiles.ProfileDTO
import it.polito.wa2.g27.server.security.AuthDTO
import it.polito.wa2.g27.server.security.AuthService
import it.polito.wa2.g27.server.ticket.TicketDTO
import it.polito.wa2.g27.server.ticket.TicketRepository
import it.polito.wa2.g27.server.ticket.TicketService
import it.polito.wa2.g27.server.ticketHistory.TicketHistoryRepository
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
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


@Testcontainers
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class MessageTests {
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
    lateinit var authService: AuthService
    @Autowired
    private lateinit var ticketRepository: TicketRepository
    @Autowired
    private lateinit var productRepository: ProductRepository
    @Autowired
    private lateinit var ticketHistoryRepository: TicketHistoryRepository
    @Autowired
    private lateinit var messageRepository: MessageRepository
    @Autowired
    private lateinit var attachmentRepository: AttachmentRepository
    @Autowired
    private lateinit var messageService: MessageService

    val user1 = ProfileDTO(1, "usertest1@mail.com", "usertest1", "Frank", "Matano", "1989-09-14" , "client", "password")
    val user2 = ProfileDTO(2, "usertest2@mail.com", "usertest2", "Marco", "Bay", "1999-10-02" , "expert", "password")

    var authenticatedProfile1: ProfileDTO? = null
    var authenticatedProfile2: ProfileDTO? = null

    var prod1 = ProductDTO("A01", "Lego Star Wars", "LEGO", "Space")
    var prod2 = ProductDTO("B01", "Lego Duplo Fashion Blogger", "LEGO Duplo", "Fashion")

    @BeforeEach
    fun populateDB() {
        authenticatedProfile1 = authService.signup(user1)
        authenticatedProfile2 = authService.createExpert(user2)
        authenticatedProfile1 = authService.login(AuthDTO(user1.email, user1.password!!))
        authenticatedProfile2 = authService.login(AuthDTO(user2.email, user2.password!!))

        val product1 = prod1.toProduct()
        val product2 = prod2.toProduct()

        productRepository.save(product1)
        productRepository.save(product2)
    }

    @AfterEach
    fun deleteDB() {
        attachmentRepository.deleteAll()
        messageRepository.deleteAll()

        ticketHistoryRepository.deleteAll()
        ticketRepository.deleteAll()

        authService.deleteProfile(user1.email)
        authService.deleteProfile(user2.email)
    }

    @Test
    fun getMessagesTest() {
        var ticket1 = TicketDTO(0, "A01", "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        ticket1 = ticketService.createTicket(ticket1)
        ticketService.assignExpert(ticket1.id, authenticatedProfile2!!.email, 2)
        val message1 = MessageDTO(1, ticket1.id, authenticatedProfile1!!.id, authenticatedProfile2!!.id, "Hello test", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), emptyList() )
        messageService.sendMessage(message1)

        val returnedMessages = messageService.getMessages(ticket1.id)
        val expectedMessages = listOf(message1)

        Assertions.assertEquals(expectedMessages[0].text, returnedMessages[0].text)
        Assertions.assertEquals(expectedMessages[0].senderId, returnedMessages[0].senderId)
        Assertions.assertEquals(expectedMessages[0].receiverId, returnedMessages[0].receiverId)
    }

    @Test
    fun postMessageTest() {
        var ticket1 = TicketDTO(0, "A01", "Lego", 0, "Non funziona", authenticatedProfile1?.id, null, null)
        ticket1 = ticketService.createTicket(ticket1)
        ticketService.assignExpert(ticket1.id, authenticatedProfile2!!.email, 2)

        val messageToSend = MessageDTO(1, ticket1.id, authenticatedProfile1!!.id, authenticatedProfile2!!.id, "Hello test", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), null )

        messageService.sendMessage(messageToSend)

        val message = messageService.getMessages(ticket1.id)
        Assertions.assertEquals(messageToSend.text, message[0].text)
        Assertions.assertEquals(messageToSend.senderId, message[0].senderId)
        Assertions.assertEquals(messageToSend.receiverId, message[0].receiverId)
    }


}
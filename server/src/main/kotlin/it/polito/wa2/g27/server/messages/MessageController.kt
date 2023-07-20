package it.polito.wa2.g27.server.messages

import WSConfig
import io.micrometer.observation.annotation.Observed
import it.polito.wa2.g27.server.messages.attachments.AttachmentDTO
import it.polito.wa2.g27.server.messages.attachments.AttachmentService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.time.LocalDateTime
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties;
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration
import org.springframework.web.socket.*
import org.springframework.web.socket.config.annotation.*

private fun createConsumer(): Consumer<String, String> {
    val props = Properties()
    props["bootstrap.servers"] = "host.docker.internal:9092"
    props["key.deserializer"] = StringDeserializer::class.java
    props["value.deserializer"] = StringDeserializer::class.java
    return KafkaConsumer(props)
}
private fun createProducer(): Producer<String, String> {
    val props = Properties()
    props["bootstrap.servers"] = "host.docker.internal:9092"
    props["group.id"] = "hello-world"
    props["key.serializer"] = StringSerializer::class.java
    props["value.serializer"] = StringSerializer::class.java
    return KafkaProducer<String, String>(props)
}
val producer = createProducer()
val wsconfig = WSConfig()

@RestController
@Observed
class MessageController(private val messageService: MessageService, private val attachmentService: AttachmentService
) {

    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/authenticated/tickets/{id}/chat")
    fun getMessages(@PathVariable id: Int, response: HttpServletResponse) : List<MessageDTO>? {

        return messageService.getMessages(id)
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/authenticated/tickets/{id}/chat/{attId}")
    fun getAttachment(@PathVariable id: Int, @PathVariable attId: Int, response: HttpServletResponse) : ResponseEntity<Resource>? {
        return attachmentService.getAttachment(attId, response)
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PostMapping("/authenticated/tickets/{id}/chat")
    @ResponseStatus(HttpStatus.CREATED)
    fun postMessage(@PathVariable id: Int,
                    @RequestParam("attachments") attachments: List<MultipartFile>,
                    @RequestParam("ticketId") ticketId: Int,
                    @RequestParam("senderId") senderId: Int,
                    @RequestParam("receiverId") receiverId: Int,
                    @RequestParam("text") text: String
    ) {

        val messageDTO = MessageDTO(0, id, senderId, receiverId, text, LocalDateTime.now(), attachments.map {
            AttachmentDTO(0, it.originalFilename ?:"", it.contentType?:"", it.size, it.bytes, 0) })
        val future = producer.send(ProducerRecord(ticketId.toString(), id.toString(), messageDTO.toString()))
        future.get()
        messageService.sendMessage(messageDTO)
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PostMapping("/authenticated/tickets/{id}/subscribe")
    @ResponseStatus(HttpStatus.CREATED)
    fun subscribe(@PathVariable id: Int) {
        val consumer = createConsumer()
        consumer.subscribe(listOf(id.toString()))

        while (true) {
            val records = consumer.poll(Duration.ofSeconds(1))
            println("Consumed ${records.count()} records")

            records.iterator().forEach {
                val message = it.value()
                println("Message: $message")

            }
        }
    }
}



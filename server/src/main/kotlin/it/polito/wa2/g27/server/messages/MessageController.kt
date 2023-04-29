package it.polito.wa2.g27.server.messages

import jakarta.validation.Valid
import netscape.javascript.JSObject
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.LocalDate
import java.time.LocalDateTime


@RestController
class MessageController(private val messageService: MessageService) {

    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/tickets/{id}/chat")
    fun getMessages(@PathVariable id: Int) : List<MessageDTO> {
        return messageService.getMessages(id)
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PostMapping("/tickets/{id}/chat")
    @ResponseStatus(HttpStatus.CREATED)
    fun postMessage(@RequestParam("attachments") attachments: List<MultipartFile>,
                    @RequestParam("ticketId") ticketId: Int,
                    @RequestParam("senderId") senderId: Int,
                    @RequestParam("receiverId") receiverId: Int,
                    @RequestParam("text") text: String
                    ) {

        println("Sono qui")
        val messageDTO = MessageDTO(0, ticketId, senderId, receiverId, text, LocalDateTime.now(), attachments)
        //println(messageDTO)
        messageService.sendMessage(messageDTO)
    }


}



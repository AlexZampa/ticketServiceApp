package it.polito.wa2.g27.server.messages

import it.polito.wa2.g27.server.exceptions.AttachmentNotFoundException
import it.polito.wa2.g27.server.exceptions.TicketNotFoundException
import it.polito.wa2.g27.server.messages.attachments.AttachmentDTO
import it.polito.wa2.g27.server.messages.attachments.AttachmentRepository
import it.polito.wa2.g27.server.messages.attachments.AttachmentService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.time.LocalDateTime
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


data class MessageReturn(
    val id : Int,
    val ticketId: Int?,
    val senderId: Int?,
    val receiverId: Int?,
    val text: String,
    val dateTime: LocalDateTime,
    val attachments: List<File>?
)

@RestController
class MessageController(private val messageService: MessageService, private val attachmentService: AttachmentService
) {

    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/tickets/{id}/chat")
    fun getMessages(@PathVariable id: Int, response: HttpServletResponse) : List<MessageDTO>? {
        return messageService.getMessages(id)
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/tickets/{id}/chat/{attId}")
    fun getAttachment(@PathVariable id: Int, @PathVariable attId: Int, response: HttpServletResponse) : ResponseEntity<Resource>? {
        return attachmentService.getAttachment(attId, response)
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
        val messageDTO = MessageDTO(0, ticketId, senderId, receiverId, text, LocalDateTime.now(), attachments.map {
            AttachmentDTO(0, it.originalFilename ?:"", it.contentType?:"", it.size, it.bytes, 0) })
        messageService.sendMessage(messageDTO)
    }
}

fun convertByteArrayToFile(byteArray: ByteArray, fileName: String): File {
    val file = File(fileName)
    val outputStream = FileOutputStream(file)
    outputStream.write(byteArray)
    outputStream.close()
    return file
}

fun createZipFile (files: Array<String>, zipFileName: String) {
    ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFileName))).use { out ->
        for (file in files) {
            FileInputStream(file).use { fi ->
                BufferedInputStream(fi).use { origin ->
                    val entry = ZipEntry(file.substring(file.lastIndexOf("/")))
                    out.putNextEntry(entry)
                    origin.copyTo(out, 1024)
                }
            }
        }
    }
}



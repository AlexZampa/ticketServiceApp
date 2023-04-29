package it.polito.wa2.g27.server.messages

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.function.RequestPredicates.contentType
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.time.LocalDateTime


data class MessageDTO(
    val id : Int,
    @field:NotNull(message = "ticketId can not be null")
    val ticketId: Int?,
    @field:NotNull(message = "senderId can not be null")
    val senderId: Int?,
    @field:NotNull(message = "receiverId can not be null")
    val receiverId: Int?,
    @field:NotEmpty(message = "Message text can not be blank")
    @field:NotNull(message = "Message text can not be null")
    val text: String,
    @field:NotEmpty(message = "Date can not be blank")
    @field:NotNull(message = "Date can not be null")
    @field:Pattern( regexp = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])\$", message = "Not valid data format")
    val dateTime: LocalDateTime,
    val attachments: List<MultipartFile>?
)

fun Message.toDTO(): MessageDTO {
    val multipartFiles = mutableListOf<MultipartFile>()
    /*
    attachments.forEach {
        // Convert the byte array into a MultipartFile



        val mockMultipartFile = MultipartFile(
            it.name,
            it.name,
            it.type,
            ByteArrayInputStream(it.data)
        )
        multipartFiles.add(mockMultipartFile)
    }
     */
    return MessageDTO(id, ticket?.id, sender?.id, receiver?.id, text, datetime, multipartFiles)
}

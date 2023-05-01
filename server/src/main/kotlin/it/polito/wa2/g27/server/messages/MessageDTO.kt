package it.polito.wa2.g27.server.messages

import it.polito.wa2.g27.server.messages.attachments.AttachmentDTO
import it.polito.wa2.g27.server.messages.attachments.toDTO
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
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
    val attachments: List<AttachmentDTO>?
)

fun Message.toDTO(): MessageDTO {
    return MessageDTO(id, ticket?.id, sender?.id, receiver?.id, text, datetime, attachments.map { it.toDTO() })
}

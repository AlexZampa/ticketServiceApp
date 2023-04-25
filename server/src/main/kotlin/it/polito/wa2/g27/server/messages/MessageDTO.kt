package it.polito.wa2.g27.server.messages

import java.time.LocalDateTime


data class MessageDTO(
    val id : Int,
    val ticketId: Int?,
    val senderId: Int?,
    val receiverId: Int?,
    val text: String,
    val dateTime: LocalDateTime
)

fun Message.toDTO(): MessageDTO {
    return MessageDTO(id, ticket?.id, sender?.id, receiver?.id, text, datetime)
}


package it.polito.wa2.g27.server.messages

import it.polito.wa2.g27.server.exceptions.ProductNotFoundException
import it.polito.wa2.g27.server.exceptions.ProfileNotFoundException
import it.polito.wa2.g27.server.exceptions.TicketNotFoundException
import it.polito.wa2.g27.server.messages.attachments.Attachment
import it.polito.wa2.g27.server.messages.attachments.AttachmentRepository
import it.polito.wa2.g27.server.messages.attachments.toAttachment
import it.polito.wa2.g27.server.profiles.Profile
import it.polito.wa2.g27.server.profiles.ProfileRepository
import it.polito.wa2.g27.server.ticket.Ticket
import it.polito.wa2.g27.server.ticket.TicketRepository
import it.polito.wa2.g27.server.ticket.toTicket
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.io.FileOutputStream

@Service @Transactional
class MessageServiceImpl(private val messageRepository: MessageRepository,
                         private val ticketRepository: TicketRepository, private val profileRepository: ProfileRepository,
                         private val attachmentRepository: AttachmentRepository
): MessageService {

    override fun getMessages(ticketId: Int): List<MessageDTO> {
        ticketRepository.findByIdOrNull(ticketId) ?: throw TicketNotFoundException("Ticket Not Found")
        return messageRepository.findAllByTicketId(ticketId).sortedBy { it.datetime }.map { it.toDTO() }
    }

    override fun sendMessage(messageDTO: MessageDTO) {
        val ticket : Ticket = ticketRepository.findByIdOrNull(messageDTO.ticketId) ?: throw TicketNotFoundException("Ticket Not Found")
        val receiver: Profile = profileRepository.findByIdOrNull(messageDTO.receiverId) ?: throw ProfileNotFoundException("Receiver not found")
        val sender: Profile = profileRepository.findByIdOrNull(messageDTO.senderId) ?: throw ProfileNotFoundException("Sender not found")
        val message: Message = messageDTO.toMessage()
        receiver.addMessageReceived(message)
        sender.addMessageSent(message)
        ticket.addMessage(message)
        messageRepository.save(message)
        messageDTO.attachments?.forEach {
            val attachment = it.toAttachment()
            message.addAttachment(attachment)
            attachmentRepository.save(attachment)
        }

    }
}
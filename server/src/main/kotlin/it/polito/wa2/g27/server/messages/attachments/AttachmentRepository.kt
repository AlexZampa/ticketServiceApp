package it.polito.wa2.g27.server.messages.attachments

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AttachmentRepository: JpaRepository<Attachment, Int> {
    fun findAllByMessageId(messageId : Int) : List<Attachment>
}
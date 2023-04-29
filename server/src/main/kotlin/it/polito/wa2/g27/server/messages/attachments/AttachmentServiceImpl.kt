package it.polito.wa2.g27.server.messages.attachments

import it.polito.wa2.g27.server.exceptions.AttachmentNotFoundException
import it.polito.wa2.g27.server.exceptions.ProfileNotFoundException
import it.polito.wa2.g27.server.exceptions.TicketNotFoundException
import it.polito.wa2.g27.server.messages.attachments.toAttachment
import it.polito.wa2.g27.server.messages.convertByteArrayToFile
import it.polito.wa2.g27.server.profiles.Profile
import it.polito.wa2.g27.server.profiles.ProfileRepository
import it.polito.wa2.g27.server.ticket.Ticket
import it.polito.wa2.g27.server.ticket.TicketRepository
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service @Transactional
class AttachmentServiceImpl(private val attachmentRepository: AttachmentRepository
): AttachmentService {
    override fun getAttachment(attId: Int, response: HttpServletResponse): ResponseEntity<Resource> {
        val attachment = attachmentRepository.findByIdOrNull(attId) ?: throw AttachmentNotFoundException("Attachment Not Found")
        val file = convertByteArrayToFile(attachment.data, attachment.name)
        val resource = FileSystemResource(file.path)

        val contentType = determineContentType(resource)
        response.contentType = contentType.toString()
        response.setContentLength(resource.contentLength().toInt())
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${resource.filename}")
        resource.inputStream.use { inputStream ->
            inputStream.copyTo(response.outputStream)
            response.flushBuffer()
        }
        return ResponseEntity.ok()
            .contentType(contentType)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${resource.filename}")
            .body(resource)
    }

    private fun determineContentType(resource: Resource): MediaType {
        val contentType = try {
            MediaType.parseMediaType(resource.file.absolutePath)
        } catch (ex: Exception) {
            MediaType.APPLICATION_OCTET_STREAM
        }
        return contentType
    }

}
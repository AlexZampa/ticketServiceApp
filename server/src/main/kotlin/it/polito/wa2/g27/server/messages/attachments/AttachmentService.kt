package it.polito.wa2.g27.server.messages.attachments

import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity

interface AttachmentService {
    fun getAttachment(attId : Int, response: HttpServletResponse): ResponseEntity<Resource>
}
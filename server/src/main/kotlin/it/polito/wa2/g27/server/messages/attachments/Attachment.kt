package it.polito.wa2.g27.server.messages.attachments

import it.polito.wa2.g27.server.messages.Message
import jakarta.persistence.*

@Entity
@Table(name = "attachments")
class Attachment {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    var id : Int = 0
    var type: String = ""
    var size: Long = 0
    @Lob
    var data: ByteArray = byteArrayOf()
    @ManyToOne(optional = false)
    var message: Message? = null
}
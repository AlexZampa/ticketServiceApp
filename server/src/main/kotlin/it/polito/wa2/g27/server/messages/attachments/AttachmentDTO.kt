package it.polito.wa2.g27.server.messages.attachments


data class AttachmentDTO(
    val id : Int,
    val name: String,
    val type: String,
    val size: Long,
    val data: ByteArray,
    val messageId: Int?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AttachmentDTO
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id
    }
}

fun Attachment.toDTO(): AttachmentDTO {
    return AttachmentDTO(id, name, type, size, data, message?.id)
}


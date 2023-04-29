package it.polito.wa2.g27.server.messages

import it.polito.wa2.g27.server.profiles.ProfileDTO

interface MessageService {
    fun getMessages(ticketId : Int): List<MessageDTO>
    fun sendMessage(messageDTO: MessageDTO)

}
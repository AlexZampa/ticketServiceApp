package it.polito.wa2.g27.server.exceptions

class TicketNotFoundException(override val message: String?): RuntimeException(message)
class TicketStatusNotValidException(override val message: String?): RuntimeException(message)
class TicketStatusException(override val message: String?): RuntimeException(message)
class TicketPriorityNotValidException(override val message: String?): RuntimeException(message)
class TicketBodyException(override val message: String?): RuntimeException(message)
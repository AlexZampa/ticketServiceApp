package it.polito.wa2.g27.server.exceptions

class ProductNotFoundException(override val message: String?): RuntimeException(message)
package it.polito.wa2.g27.server.exceptions


class ProfileNotFoundException(override val message: String?): RuntimeException(message)

class ProfileAlreadyExistsException(override val message: String?): RuntimeException(message)

class ProfileEmailModificationException(override val message: String?): RuntimeException(message)

class ProfileAuthenticationException(override val message: String?): RuntimeException(message)
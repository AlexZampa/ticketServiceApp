package it.polito.wa2.g27.server.security

data class UserDTO(
    val token: String,
    val email: String,
    val username: String,
    val name: String?,
    val surname: String?
)
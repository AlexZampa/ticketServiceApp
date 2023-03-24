package it.polito.wa2.g27.server.profiles

data class ProfileDTO (
    val id: String,
    val email: String,
    val username: String,
    val hash: String
)
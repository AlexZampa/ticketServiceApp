package it.polito.wa2.g27.server.security

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class AuthDTO (
    @field:NotNull
    @field:NotBlank
    val username: String,

    @field:NotNull
    @field:NotBlank
    val password: String
)
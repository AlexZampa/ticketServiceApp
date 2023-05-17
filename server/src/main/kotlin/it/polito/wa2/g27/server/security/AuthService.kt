package it.polito.wa2.g27.server.security

import org.springframework.http.HttpStatusCode

interface AuthService {
    fun login(authDTO: AuthDTO): UserDTO
    fun logout(token: String): HttpStatusCode
}
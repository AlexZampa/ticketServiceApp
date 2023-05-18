package it.polito.wa2.g27.server.security

import it.polito.wa2.g27.server.exceptions.ProfileAuthenticationException
import it.polito.wa2.g27.server.exceptions.ProfileNotFoundException
import it.polito.wa2.g27.server.profiles.ProfileRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestTemplate

class CustomErrorHandler: ResponseErrorHandler {
    override fun hasError(response: ClientHttpResponse): Boolean {
        return response.statusCode == HttpStatus.UNAUTHORIZED
    }

    override fun handleError(response: ClientHttpResponse) {
        // Implementa la logica per gestire l'errore
        // Puoi accedere allo status code, al corpo della risposta, agli header, ecc.
        val statusCode = response.statusCode
        val responseBody = response.body
        // Esegui le azioni desiderate per gestire l'errore
    }
}


@Service
class AuthServiceImpl(
    @Value("\${keycloack.auth.token}") private val keycloackUrlToken: String,
    @Value("\${keycloack.auth.logout}") private val keycloackUrlLogout: String,
    @Value("\${jwt.auth.converter.resource-id}") private val clientId: String,
    private val profileRepository: ProfileRepository,
    private val jwtAuthConverter: JwtAuthConverter
): AuthService {

    override fun login(authDTO: AuthDTO): UserDTO {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val request = HttpEntity(
            "grant_type=password&client_id=${clientId}&username=${authDTO.username}&password=${authDTO.password}",
            httpHeaders
        )

        val restTemplate = RestTemplateBuilder().build()
        try {
            val response: ResponseEntity<Map<String, Any>> = restTemplate.exchange(
                keycloackUrlToken, HttpMethod.POST, request,
                object : ParameterizedTypeReference<Map<String, Any>>() {})

            val token = response.body?.get("access_token") as String
            val email = jwtAuthConverter.getEmail(token)
            val username = jwtAuthConverter.getUsername(token)

            val profile = profileRepository.findByEmail(email) ?: throw ProfileNotFoundException("Profile Not Found")

            return UserDTO(token, email, username, profile.name, profile.surname)
        }catch (e: HttpClientErrorException){
            throw ProfileAuthenticationException("Invalid Credentials")
        }
    }

    override fun logout(token: String): HttpStatusCode {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED
        httpHeaders.set("Authorization", token)
        val request = HttpEntity("", httpHeaders)
        val restTemplate = RestTemplate()
        val response: ResponseEntity<String> = restTemplate.exchange(keycloackUrlLogout, HttpMethod.GET, request, String::class.java)

        return response.statusCode
    }

}
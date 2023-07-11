package it.polito.wa2.g27.server.security

import it.polito.wa2.g27.server.exceptions.ProfileAlreadyExistsException
import it.polito.wa2.g27.server.exceptions.ProfileAuthenticationException
import it.polito.wa2.g27.server.exceptions.ProfileNotFoundException
import it.polito.wa2.g27.server.profiles.*
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.springframework.http.HttpStatusCode
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.http.HttpStatus



@Service
class AuthServiceImpl(
    private  val properties: AuthServiceProperties,
    private val profileRepository: ProfileRepository,
    private val jwtAuthConverter: JwtAuthConverter
): AuthService {

    override fun login(authDTO: AuthDTO): ProfileDTO {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val request = HttpEntity(
            "grant_type=password&client_id=${properties.clientId}&username=${authDTO.username}&password=${authDTO.password}",
            httpHeaders
        )

        val restTemplate = RestTemplateBuilder().build()
        try {
            val response: ResponseEntity<Map<String, Any>> = restTemplate.exchange(
                properties.tokenUrl!!, HttpMethod.POST, request,
                object : ParameterizedTypeReference<Map<String, Any>>() {})

            val token = response.body?.get("access_token") as String
            val email = jwtAuthConverter.getEmail(token)

            val profile = profileRepository.findByEmail(email) ?: throw ProfileNotFoundException("Profile Not Found")
            val profileDTO = profile.toDTO()
            profileDTO.token = token
            return profileDTO
        }catch (e: HttpClientErrorException){
            throw ProfileAuthenticationException("Invalid Credentials")
        }
    }

    override fun signup(profileDTO: ProfileDTO): ProfileDTO {
        val keycloak = createKeycloakAdminClient()
        val realmResource = keycloak.realm(properties.realm)

        // Create a UserRepresentation object and set the user details
        val user = createUserRepresentation(profileDTO)

        // Create a CredentialRepresentation object and set the password
        val credential = createCredentialRepresentation(profileDTO.password)

        // Add the credential to the user
        user.credentials = listOf(credential)

        // Create the user in Keycloak
        val response = keycloak.realm(properties.realm).users().create(user)

        // if error
        if(response.status != HttpStatus.CREATED.value())
            throw ProfileAlreadyExistsException("Profile Already Exists")

        // get user UUID
        val userId = response.location.toString().split("/").last()

        // Assign a realm-level role to the user
        val roleRepresentation = realmResource.roles().get("app-client").toRepresentation()
        keycloak.realm(properties.realm).users().get(userId).roles().realmLevel().add(mutableListOf(roleRepresentation))

        val newProfile = profileRepository.save(profileDTO.toProfile())
        return newProfile.toDTO()
    }

    override fun logout(token: String): HttpStatusCode {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED
        httpHeaders.set("Authorization", token)
        val request = HttpEntity("", httpHeaders)
        val restTemplate = RestTemplate()

        val response: ResponseEntity<String> = restTemplate.exchange(properties.logoutUrl!!, HttpMethod.GET, request, String::class.java)
        return response.statusCode
    }

    override fun createExpert(profileDTO: ProfileDTO): ProfileDTO {
        val keycloak = createKeycloakAdminClient()
        val realmResource = keycloak.realm(properties.realm)

        // Create a UserRepresentation object and set the user details
        val user = createUserRepresentation(profileDTO)

        // Create a CredentialRepresentation object and set the password
        val credential = createCredentialRepresentation(profileDTO.password)

        // Add the credential to the user
        user.credentials = listOf(credential)

        // Create the user in Keycloak
        val response = keycloak.realm(properties.realm).users().create(user)

        // if error
        if(response.status != HttpStatus.CREATED.value())
            throw ProfileAlreadyExistsException("Profile Already Exists")

        // get user UUID
        val userId = response.location.toString().split("/").last()

        // Assign a realm-level role to the user
        val roleRepresentation = realmResource.roles().get("app-expert").toRepresentation()
        keycloak.realm(properties.realm).users().get(userId).roles().realmLevel().add(mutableListOf(roleRepresentation))

        val newProfile = profileRepository.save(profileDTO.toProfile())
        return newProfile.toDTO()
    }

    // keycloak factory
    private fun createKeycloakAdminClient(): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(properties.serverUrl)
            .grantType(OAuth2Constants.PASSWORD)
            .realm("master")
            .username(properties.username)
            .password(properties.password)
            .clientId("admin-cli")
            .build()
    }

    private fun createUserRepresentation(profileDTO: ProfileDTO): UserRepresentation{
        val user = UserRepresentation()
        println(profileDTO.username)
        user.username = profileDTO.username
        user.email = profileDTO.email
        user.firstName = profileDTO.name
        user.lastName = profileDTO.surname
        user.isEmailVerified = true
        user.isEnabled = true
        return user
    }

    private fun createCredentialRepresentation(password: String? = ""): CredentialRepresentation{
        val credential = CredentialRepresentation()
        credential.isTemporary = false
        credential.type = CredentialRepresentation.PASSWORD
        credential.value = password
        return credential
    }

}
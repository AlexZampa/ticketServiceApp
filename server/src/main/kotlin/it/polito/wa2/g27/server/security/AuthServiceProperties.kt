package it.polito.wa2.g27.server.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

@Validated
@Configuration
@ConfigurationProperties(prefix = "keycloak")
data class AuthServiceProperties (
    var serverUrl: String? = null,
    var realm: String? = null,
    var username: String? = null,
    var password: String? = null,
    var clientId: String? = null,
    var tokenUrl: String? = null,
    var logoutUrl: String? = null
)

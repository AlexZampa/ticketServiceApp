package it.polito.wa2.g27.server.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.stream.Collectors
import java.util.stream.Stream


@Component
class JwtAuthConverter(

    private val properties: JwtAuthConverterProperties
) :
    Converter<Jwt?, AbstractAuthenticationToken?> {

    private val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()

    override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        val authorities: Collection<GrantedAuthority> = Stream.concat(
            jwtGrantedAuthoritiesConverter.convert(jwt)!!.stream(),
            extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet())
        return JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt))
    }

    private fun getPrincipalClaimName(jwt: Jwt): String {
        var claimName: String? = JwtClaimNames.SUB
        if (properties.principalAttribute != null) {
            claimName = properties.principalAttribute
        }
        return jwt.getClaim(claimName)
    }

    private fun extractResourceRoles(jwt: Jwt): Collection<GrantedAuthority> {
        val resourceAccess = jwt.getClaim<Map<String, Any>>("resource_access")
        val resource = resourceAccess?.get(properties.resourceId) as? Map<*, *>
        val resourceRoles = resource?.get("roles") as? Collection<*>
        //val resourceAccess: Map<String, Any>? = jwt.getClaim("resource_access")
        //val resource: Map<String?, Any?>? = resourceAccess?.get(properties.resourceId) as MutableMap<String?, Any?>?
        //val resourceRoles: Collection<String?>? = resource?.get("roles") as Collection<String?>?

        return if (resourceAccess == null || resource == null || resourceRoles == null) {
            setOf<GrantedAuthority>()
        } else resourceRoles.stream()
            .map<SimpleGrantedAuthority> { role ->
                SimpleGrantedAuthority("ROLE_$role")
            }
            .collect(Collectors.toSet())
    }

    private fun convertStringToJwt(accessTokenString: String): Jwt {
        val jwtDecoder: JwtDecoder = NimbusJwtDecoder.withJwkSetUri(properties.jwkSetUri).build()
        return jwtDecoder.decode(accessTokenString)
    }

    fun getRole(accessToken: String): String {
        val jwt = this.convertStringToJwt(accessToken)
        val clientAccess = jwt.getClaim<Map<*, *>>("resource_access")[properties.resourceId] as Map<*, *>
        return (clientAccess["roles"] as List<String>)[0]
    }

    fun getUUID(accessToken: String): UUID {
        val jwt = this.convertStringToJwt(accessToken)
        return UUID.fromString(jwt.getClaim<String>("sub"))
    }

    fun getEmail(accessToken: String): String {
        val jwt = this.convertStringToJwt(accessToken)
        return jwt.getClaim<String>("email")
    }

    fun getUsername(accessToken: String): String {
        val jwt = this.convertStringToJwt(accessToken)
        return jwt.getClaim<String>("preferred_username")
    }
}
package it.polito.wa2.g27.server.security

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.stereotype.Component
import java.util.stream.Collectors
import java.util.stream.Stream


@Component
class JwtAuthConverter(private val properties: JwtAuthConverterProperties) :
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
        println("CIAO 1")
        val resourceAccess: Map<String, Any>? = jwt.getClaim("resource_access")
        val resource: Map<String?, Any?>? = resourceAccess?.get(properties.resourceId) as MutableMap<String?, Any?>?
        val resourceRoles: Collection<String?>? = resource?.get("roles") as Collection<String?>?
        println("CIAO 2")
        println(resourceAccess)
        println(resource)
        println(resourceRoles)

        return if (resourceAccess == null || resource == null || resourceRoles == null) {
            setOf<GrantedAuthority>()
        } else resourceRoles.stream()
            .map<SimpleGrantedAuthority> { role ->
                SimpleGrantedAuthority("ROLE_$role")
            }
            .collect(Collectors.toSet())
    }
}
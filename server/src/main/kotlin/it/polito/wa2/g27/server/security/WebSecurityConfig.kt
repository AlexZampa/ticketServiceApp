package it.polito.wa2.g27.server.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain


// @RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
class WebSecurityConfig (private val jwtAuthConverter: JwtAuthConverter){

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors().and().csrf().disable()
            .authorizeHttpRequests()
            //.requestMatchers(HttpMethod.GET, "/test/anonymous", "/test/anonymous/**").permitAll()
            //.requestMatchers(HttpMethod.GET, "/test/admin", "/test/admin/**").permitAll()
            //.requestMatchers(HttpMethod.GET, "/test/user").hasAnyRole(MANAGER, USER)
            .requestMatchers("/public/**").permitAll()
            .requestMatchers("/authenticated/**").authenticated()
            .requestMatchers("/test/client/**").hasRole(CLIENT)
            .requestMatchers("/test/expert/**").hasRole(EXPERT)
            .requestMatchers("/test/manager/**").hasRole(MANAGER)
            .anyRequest().permitAll()
        //.anyRequest().authenticated()
        http.oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthConverter)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        return http.build()
    }

    companion object {
        const val MANAGER = "manager"
        const val CLIENT = "client"
        const val EXPERT = "expert"
    }
}
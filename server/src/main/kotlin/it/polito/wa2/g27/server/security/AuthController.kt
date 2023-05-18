package it.polito.wa2.g27.server.security

import org.springframework.http.HttpStatusCode
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController
class AuthController(private val authService: AuthService) {

    @PostMapping("/public/login")
    fun login(@RequestBody authDTO: AuthDTO, br: BindingResult): Any {
        //if (br.hasErrors())
          //  throw ValidationException(br.fieldErrors, "Validation Errors")

        return authService.login(authDTO)
    }

    @DeleteMapping("/authenticated/logout")
    fun logout(@RequestHeader("Authorization") token: String): HttpStatusCode {
        return authService.logout(token)
    }

}
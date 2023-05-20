package it.polito.wa2.g27.server.security

import it.polito.wa2.g27.server.profiles.ProfileDTO
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController
class AuthController(private val authService: AuthService) {

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PostMapping("/public/login")
    fun login(@RequestBody authDTO: AuthDTO, br: BindingResult): ProfileDTO? {
        return authService.login(authDTO)
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PostMapping("/public/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@Valid @RequestBody profileDTO: ProfileDTO): ProfileDTO {
        return authService.signup(profileDTO)
    }

    @CrossOrigin(origins = ["http://localhost:3000"])
    @DeleteMapping("/authenticated/logout")
    fun logout(@RequestHeader("Authorization") token: String) {
        authService.logout(token)
    }

}
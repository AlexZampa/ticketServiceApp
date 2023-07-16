package it.polito.wa2.g27.server.security

import io.micrometer.observation.annotation.Observed
import it.polito.wa2.g27.server.profiles.ProfileDTO
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController
@Observed
class AuthController(private val authService: AuthService) {

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PostMapping("/public/login")
    fun login(@RequestBody authDTO: AuthDTO, br: BindingResult): ProfileDTO? {
        return authService.login(authDTO)
    }

    @PutMapping("/authenticated/profiles/{email}")
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun modifyProfile(@PathVariable email: String,
                      @RequestHeader("Authorization") token: String,
                      @Valid @RequestBody profileDTO: ProfileDTO) {
        return authService.modifyProfile(token, profileDTO)
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

    @CrossOrigin(origins = ["http://localhost:3000"])
    @PostMapping("/manager/createExpert")
    @ResponseStatus(HttpStatus.CREATED)
    fun signupExpert(@Valid @RequestBody profileDTO: ProfileDTO): ProfileDTO {
        return authService.createExpert(profileDTO)
    }

}
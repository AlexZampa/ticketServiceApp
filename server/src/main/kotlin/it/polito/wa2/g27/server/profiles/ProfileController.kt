package it.polito.wa2.g27.server.profiles

import io.micrometer.observation.annotation.Observed
import jakarta.validation.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@Observed
class ProfileController(private val profileService: ProfileService) {
    @GetMapping("/authenticated/profiles/{email}")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun getByEmail(@PathVariable email: String): ProfileDTO? {
        return profileService.getByEmail(email)
    }

    @PutMapping("/authenticated/profiles/{email}")
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun modifyProfile(@PathVariable email: String, @Valid @RequestBody profileDTO: ProfileDTO) {
        return profileService.modifyProfile(email, profileDTO)
    }
}

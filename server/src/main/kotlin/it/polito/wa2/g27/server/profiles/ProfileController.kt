package it.polito.wa2.g27.server.profiles

import jakarta.validation.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class ProfileController(private val profileService: ProfileService) {
    @GetMapping("/profiles/{email}")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun getByEmail(@PathVariable email: String): ProfileDTO? {
        return profileService.getByEmail(email)
    }

    @PostMapping("/profiles")
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun createProfile(@Valid @RequestBody profileDTO: ProfileDTO) {
        return profileService.createProfile(profileDTO)
    }

    @PutMapping("/profiles/{email}")
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun modifyProfile(@PathVariable email: String, @Valid @RequestBody profileDTO: ProfileDTO) {
        return profileService.modifyProfile(email, profileDTO)
    }
}

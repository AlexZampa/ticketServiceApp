package it.polito.wa2.g27.server.profiles

import io.micrometer.observation.annotation.Observed
import jakarta.validation.*
import org.springframework.web.bind.annotation.*

@RestController
@Observed
class ProfileController(private val profileService: ProfileService) {

    @GetMapping("/authenticated/profiles/id/{id}")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun getById(@PathVariable id: Int): ProfileDTO? {
        return profileService.getById(id)
    }

    @GetMapping("/authenticated/profiles/{email}")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun getByEmail(@PathVariable email: String): ProfileDTO? {
        return profileService.getByEmail(email)
    }

    @GetMapping("/manager/profiles/expert")
    @CrossOrigin(origins = ["http://localhost:3000"])
    fun getAllByRole(): List<ProfileDTO> {
        return profileService.getAllByRole("expert")
    }

}

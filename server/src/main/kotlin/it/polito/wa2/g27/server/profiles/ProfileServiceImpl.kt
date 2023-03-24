package it.polito.wa2.g27.server.profiles

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ProfileServiceImpl(private val profileRepository: ProfileRepository): ProfileService {
    override fun getByEmail(email: String): ProfileDTO? {
        return profileRepository.findByIdOrNull(email)?.toDTO()
    }

    override fun createProfile(
        email: String,
        username: String,
        name: String,
        surname: String,
        dateOfBirth: LocalDate,
        password: String
    ) {
        TODO("Not yet implemented")
    }

    override fun modifyProfile(email: String, username: String, name: String, surname: String, dateOfBirth: LocalDate) {
        TODO("Not yet implemented")
    }
}
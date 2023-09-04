package it.polito.wa2.g27.server.profiles

import it.polito.wa2.g27.server.exceptions.ProfileNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProfileServiceImpl(private val profileRepository: ProfileRepository): ProfileService {
    override fun getByEmail(email: String): ProfileDTO {
        return profileRepository.findByEmail(email)?.toDTO() ?: throw ProfileNotFoundException("Profile not found")
    }

    override fun getById(id: Int): ProfileDTO {
        return profileRepository.findByIdOrNull(id)?.toDTO() ?: throw ProfileNotFoundException("Profile not found")
    }


    override fun getAllByRole(role: String): List<ProfileDTO> {
        return profileRepository.findAllByRole(role).map{ it.toDTO() }
    }
}
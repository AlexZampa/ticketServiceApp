package it.polito.wa2.g27.server.profiles

import it.polito.wa2.g27.server.exceptions.ProfileAlreadyExistsException
import it.polito.wa2.g27.server.exceptions.ProfileEmailModificationException
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

    override fun createProfile(profileDTO: ProfileDTO): ProfileDTO {
        val oldProfile = profileRepository.findByEmail(profileDTO.email)
        if(oldProfile != null){
            throw ProfileAlreadyExistsException("Profile Already Exists")
        }
        val newProfile: Profile = profileRepository.save(profileDTO.toProfile())
        return newProfile.toDTO()
    }

    override fun modifyProfile(email: String, profileDTO: ProfileDTO) {
        if(profileDTO.email != email){
            throw ProfileEmailModificationException("Email Update not possible")
        }

        val p = profileRepository.findByEmail(email) ?: throw ProfileNotFoundException("Profile Not Found")
        profileRepository.save(profileDTO.toProfile())
    }

}
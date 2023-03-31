package it.polito.wa2.g27.server.profiles

import it.polito.wa2.g27.server.exceptions.ProfileAlreadyExistsException
import it.polito.wa2.g27.server.exceptions.ProfileEmailModificationException
import it.polito.wa2.g27.server.exceptions.ProfileNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ProfileServiceImpl(private val profileRepository: ProfileRepository): ProfileService {
    override fun getByEmail(email: String): ProfileDTO? {
        return profileRepository.findByIdOrNull(email)?.toDTO() ?: throw ProfileNotFoundException("Profile not found")
    }

    override fun createProfile(profileDTO: ProfileDTO) {
        val oldProfile = profileRepository.findByIdOrNull(profileDTO.email)
        if(oldProfile != null){
            throw ProfileAlreadyExistsException("Profile Already Exists")
        }

        val p = Profile()
        p.email = profileDTO.email
        p.username = profileDTO.username
        p.name = profileDTO.name
        p.surname = profileDTO.surname
        p.dateofbirth = LocalDate.parse(profileDTO.dateOfBirth)
        p.hash = profileDTO.hash
        profileRepository.save(p)
    }

    override fun modifyProfile(email: String, profileDTO: ProfileDTO) {
        if(profileDTO.email != email){
            throw ProfileEmailModificationException("Email Update not possible")
        }

        val p = profileRepository.findByIdOrNull(email) ?: throw ProfileNotFoundException("Profile Not Found")
        p.email = profileDTO.email
        p.username = profileDTO.username
        p.name = profileDTO.name
        p.surname = profileDTO.surname
        p.dateofbirth = LocalDate.parse(profileDTO.dateOfBirth)
        p.hash = profileDTO.hash
        profileRepository.save(p)
    }
}
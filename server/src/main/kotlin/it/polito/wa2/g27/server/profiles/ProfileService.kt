package it.polito.wa2.g27.server.profiles


interface ProfileService {
    fun getByEmail(email: String): ProfileDTO?
    fun createProfile(profileDTO: ProfileDTO)
    fun modifyProfile(email: String, profileDTO: ProfileDTO)

}
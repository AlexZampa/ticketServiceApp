package it.polito.wa2.g27.server.security

import it.polito.wa2.g27.server.profiles.ProfileDTO
import org.springframework.http.HttpStatusCode

interface AuthService {
    fun login(authDTO: AuthDTO): ProfileDTO
    fun signup(profileDTO: ProfileDTO): ProfileDTO
    fun logout(token: String): HttpStatusCode
    fun modifyProfile(token: String, profileDTO: ProfileDTO)
    fun createExpert(profileDTO: ProfileDTO): ProfileDTO
    fun createManager(profileDTO: ProfileDTO): ProfileDTO
    fun deleteProfile(email:String)
}
package it.polito.wa2.g27.server.profiles

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfileRepository: JpaRepository<Profile, Int> {
    fun findByEmail(email: String): Profile?
    fun findAllByRole(role: String): List<Profile>
}
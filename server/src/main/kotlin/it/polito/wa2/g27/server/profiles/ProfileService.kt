package it.polito.wa2.g27.server.profiles

import java.time.LocalDate

interface ProfileService {
    fun getByEmail(email: String): ProfileDTO?
    fun createProfile(email: String, username: String, name: String, surname: String, dateOfBirth: LocalDate, password: String): Unit

    fun modifyProfile(email: String, username: String, name: String, surname: String, dateOfBirth: LocalDate): Unit

}
package it.polito.wa2.g27.server.profiles

import java.time.LocalDate

data class ProfileDTO (
    var email: String,
    var username: String,
    var name: String,
    var surname: String,
    var dateOfBirth: LocalDate,
    var hash: String
)

fun Profile.toDTO() : ProfileDTO{
    return ProfileDTO(email, username, name, surname, dateofbirth, hash)
}
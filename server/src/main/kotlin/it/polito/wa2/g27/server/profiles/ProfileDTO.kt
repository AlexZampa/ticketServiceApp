package it.polito.wa2.g27.server.profiles

import org.jetbrains.annotations.NotNull

data class ProfileDTO (
    @field:NotNull var email: String,
    var username: String,
    var name: String,
    var surname: String,
    var dateOfBirth: String,
    var hash: String
)

fun Profile.toDTO() : ProfileDTO{
    return ProfileDTO(email, username, name, surname, dateofbirth.toString(), hash)
}
package it.polito.wa2.g27.server.profiles

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class ProfileDTO (
    @field:Email(message = "Email not valid") var email: String,
    @field:NotNull(message = "Username can not be blank") var username: String,
    @field:NotNull(message = "Name can not be blank") var name: String,
    @field:NotNull(message = "Surname can not be blank") var surname: String,
    @field:Pattern( regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])\$", message = "Not valid data format") var dateOfBirth: String,
    @field:NotNull(message = "Hash can not be blank") var hash: String
)

fun Profile.toDTO() : ProfileDTO{
    return ProfileDTO(email, username, name, surname, dateofbirth.toString(), hash)
}
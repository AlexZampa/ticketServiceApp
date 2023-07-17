package it.polito.wa2.g27.server.profiles

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class ProfileDTO (
    var id: Int?,
    @field:NotEmpty(message = "Email can not be blank")
    @field:NotNull(message = "Email can not be null")
    @field:Email(message = "Email not valid") var email: String,
    @field:NotEmpty(message = "Username can not be blank")
    @field:NotNull(message = "Username can not be null") var username: String,
    @field:NotEmpty(message = "Name can not be blank")
    @field:NotNull(message = "Name can not be null") var name: String,
    @field:NotEmpty(message = "Surname can not be blank")
    @field:NotNull(message = "Surname can not be null") var surname: String,
    @field:NotEmpty(message = "Date can not be blank")
    @field:NotNull(message = "Date can not be null")
    @field:Pattern( regexp = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])\$", message = "Not valid data format") var dateOfBirth: String,
    @field:NotEmpty(message = "Role can not be blank")
    @field:NotNull(message = "Role can not be null") var role: String,
    var password: String? = null,
    var token: String? = null
)

fun Profile.toDTO() : ProfileDTO{
    return ProfileDTO(id, email, username, name, surname, dateofbirth.toString(), role, "", "")
}
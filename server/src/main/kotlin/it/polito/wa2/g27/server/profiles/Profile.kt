package it.polito.wa2.g27.server.profiles

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "profiles")
class Profile {
    @Id
    var email: String = ""
    var username: String = ""
    var name: String = ""
    var surname: String = ""
    var dateofbirth: LocalDate = LocalDate.now()
    var hash: String = ""
}
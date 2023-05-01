package it.polito.wa2.g27.server.integrationTests

import it.polito.wa2.g27.server.profiles.Profile
import it.polito.wa2.g27.server.profiles.ProfileDTO
import it.polito.wa2.g27.server.profiles.ProfileRepository
import it.polito.wa2.g27.server.profiles.ProfileService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Testcontainers
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class UserTests {
    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:latest")
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.jpa.hibernate.ddl-auto") {"create-drop"}
        }
    }

    @Autowired
    lateinit var profileService: ProfileService
    @Autowired
    private lateinit var profileRepository: ProfileRepository

    val user1 = ProfileDTO(1, "user1@mail.com", "user1", "Frank", "Matano", "1989-09-14" )
    val user2 = ProfileDTO(2, "user2@mail.com", "user2", "Marco", "Bay", "1999-10-02" )


    @BeforeEach
    fun populateDB() {
        val profile1 = Profile().apply {
            id = 1; email = user1.email
            name = user1.name; surname = user1.surname; username = user1.username
            dateofbirth = LocalDate.parse(user1.dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

        val profile2 = Profile().apply {
            id = 2; email = user2.email
            name = user2.name; surname = user2.surname; username = user2.username
            dateofbirth = LocalDate.parse(user2.dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

        profileRepository.delete(profile1)
        profileRepository.delete(profile2)
        profileRepository.save(profile1)
        profileRepository.save(profile2)

    }


    @Test
    fun getUserByEmailTest() {
        val profileDTO = profileService.getByEmail(user2.email)
        Assertions.assertEquals(user2, profileDTO)
    }

    @Test
    fun createUserTest() {
        val profileDTO = ProfileDTO(null, "marco.bay@gmail.com", "marco_seaside", "Marco", "Bay", "1999-10-02")
        val newProfileDTO = profileService.createProfile(profileDTO)
        profileDTO.id = newProfileDTO.id

        Assertions.assertEquals(profileDTO, newProfileDTO)
    }

    @Test
    fun modifyUserTest() {
        val profileDTO = profileService.getByEmail(user1.email)
        val newProfileDTO = ProfileDTO(profileDTO.id, profileDTO.email, "f_mark", "Franky", "Marky", "1999-10-02")

        profileService.modifyProfile(profileDTO.email, newProfileDTO)

        val modifiedProfileDTO = profileService.getByEmail(profileDTO.email)
        Assertions.assertEquals(newProfileDTO, modifiedProfileDTO)
    }
}

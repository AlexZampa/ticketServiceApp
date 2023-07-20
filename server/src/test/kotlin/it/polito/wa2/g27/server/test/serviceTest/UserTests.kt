package it.polito.wa2.g27.server.test.serviceTest

import it.polito.wa2.g27.server.exceptions.ProfileAlreadyExistsException
import it.polito.wa2.g27.server.exceptions.ProfileEmailModificationException
import it.polito.wa2.g27.server.exceptions.ProfileNotFoundException
import it.polito.wa2.g27.server.profiles.*
import it.polito.wa2.g27.server.security.AuthDTO
import it.polito.wa2.g27.server.security.AuthService
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
    lateinit var authService: AuthService

    val user1 = ProfileDTO(1, "usertest1@mail.com", "usertest1", "Frank", "Matano", "1989-09-14" , "client", "password")
    val user2 = ProfileDTO(2, "usertest2@mail.com", "usertest2", "Marco", "Bay", "1999-10-02" , "expert", "password")
    val user3 = ProfileDTO(null, "marco.bay@gmail.com", "marco_seaside", "Marco", "Bay", "1999-10-02", "client", "password")

    var authenticatedProfile1: ProfileDTO? = null
    var authenticatedProfile2: ProfileDTO? = null

    @BeforeEach
    fun populateDB() {
        authService.deleteProfile(user1.email)
        authService.deleteProfile(user2.email)
        authService.deleteProfile(user3.email)
        authenticatedProfile1 = authService.signup(user1)
        authenticatedProfile2 = authService.signup(user2)
        authenticatedProfile1 = authService.login(AuthDTO(user1.email, user1.password!!))
        authenticatedProfile2 = authService.login(AuthDTO(user2.email, user2.password!!))
    }

    @Test
    fun getUserByEmailTest() {
        val profileDTO = profileService.getByEmail(user2.email)
        user2.id = profileDTO.id
        profileDTO.password=user2.password
        profileDTO.token=user2.token
        Assertions.assertEquals(user2, profileDTO)
    }

    @Test
    fun createUserTest() {
        val newProfileDTO = authService.signup(user3)
        user3.id = newProfileDTO.id
        user3.password=""
        user3.token=null
        newProfileDTO.token=null

        Assertions.assertEquals(user3, newProfileDTO)
    }

    @Test
    fun modifyUserTest() {
        val profileDTO = profileService.getByEmail(user1.email)
        val newProfileDTO = ProfileDTO(profileDTO.id, profileDTO.email, "f_mark", "Franky", "Marky", "1999-10-02", "client", "password")

        authService.modifyProfile("Bearer " + authenticatedProfile1?.token!!, newProfileDTO)

        val modifiedProfileDTO = profileService.getByEmail(profileDTO.email)
        newProfileDTO.password=""
        newProfileDTO.token=null
        modifiedProfileDTO.token=null
        Assertions.assertEquals(newProfileDTO, modifiedProfileDTO)
    }

    @Test
    fun getUserByEmailNotExists() {

        ProfileNotFoundException("Profile not found")
        Assertions.assertThrows(ProfileNotFoundException::class.java, {
            profileService.getByEmail("")
        }, "Profile not found")
    }

    @Test
    fun profileAlreadyExists() {

        val profileDTO = ProfileDTO(null, "usertest1@mail.com", "marco_seaside", "Marco", "Bay", "1999-10-02", "client")

        Assertions.assertThrows(ProfileAlreadyExistsException::class.java, {
            authService.signup(profileDTO)
        }, "Profile Already exists")
    }

    @Test
    fun modifyUserEmail() {
        val profileDTO = profileService.getByEmail(user1.email)
        val newProfileDTO = ProfileDTO(profileDTO.id, profileDTO.email + "###", "f_mark", "Franky", "Marky", "1999-10-02", "client")

        Assertions.assertThrows(ProfileEmailModificationException::class.java, {
            authService.modifyProfile("Bearer " + authenticatedProfile1?.token!!, newProfileDTO)
        }, "Email Update not possible")
    }

}

package it.polito.wa2.g27.server.test.integrationTest
import it.polito.wa2.g27.server.profiles.ProfileDTO
import it.polito.wa2.g27.server.profiles.ProfileRepository
import it.polito.wa2.g27.server.profiles.ProfileService
import it.polito.wa2.g27.server.profiles.toProfile
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IntegrationProfileTest {
    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:latest")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.jpa.hibernate.ddl-auto") { "create-drop" }
        }

    }

    @LocalServerPort
    protected var port: Int = 0

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var profileService: ProfileService
    @Autowired
    private lateinit var profileRepository: ProfileRepository

    val user1 = ProfileDTO(1, "user1@mail.com", "user1", "Frank", "Matano", "1989-09-14" )
    val user2 = ProfileDTO(2, "user2@mail.com", "user2", "Marco", "Bay", "1999-10-02" )


    @BeforeEach
    fun populateDB() {
        val profile1 = user1.toProfile()
        val profile2 = user2.toProfile()

        profileRepository.deleteAll()
        profileRepository.save(profile1)
        profileRepository.save(profile2)

    }

    @Test
    fun getUserByEmailTest() {
        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/profiles/${user1.email}",
            HttpMethod.GET,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun createUserTest() {
        val profileDTO = ProfileDTO(null, "marco.bay@gmail.com", "marco_seaside", "Marco", "Bay", "1999-10-02")

        val headers = HttpHeaders()
        val requestEntity= HttpEntity<ProfileDTO>(profileDTO, headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/profiles",
            HttpMethod.POST,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
    }

    @Test
    fun modifyUserTest() {
        val profileDTO = profileService.getByEmail(user1.email)
        val newProfileDTO = ProfileDTO(profileDTO.id, profileDTO.email, "f_mark", "Franky", "Marky", "1999-10-02")

        val headers = HttpHeaders()
        val requestEntity= HttpEntity<ProfileDTO>(newProfileDTO, headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/profiles/${profileDTO.email}",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
    }

    @Test
    fun getUserByEmailNotExists() {
        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/profiles/marco.bay@gmail.com",
            HttpMethod.GET,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun profileAlreadyExists() {

        val profileDTO = ProfileDTO(null, "user1@mail.com", "marco_seaside", "Marco", "Bay", "1999-10-02")

        val headers = HttpHeaders()
        val requestEntity= HttpEntity<ProfileDTO>(profileDTO, headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/profiles",
            HttpMethod.POST,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.CONFLICT, response.statusCode)
    }

}
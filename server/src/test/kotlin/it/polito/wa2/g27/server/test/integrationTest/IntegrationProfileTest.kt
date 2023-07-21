package it.polito.wa2.g27.server.test.integrationTest
import it.polito.wa2.g27.server.profiles.ProfileDTO
import it.polito.wa2.g27.server.profiles.ProfileService
import it.polito.wa2.g27.server.security.AuthDTO
import it.polito.wa2.g27.server.security.AuthService
import org.junit.jupiter.api.AfterEach
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
    lateinit var authService: AuthService

    val user1 = ProfileDTO(1, "usertest1@mail.com", "usertest1", "Frank", "Matano", "1989-09-14" , "client", "password")
    val user2 = ProfileDTO(2, "usertest2@mail.com", "usertest2", "Marco", "Bay", "1999-10-02" , "manager", "password")
    val user3 = ProfileDTO(3, "usertest3@mail.com", "usertest3", "Mary", "White", "1992-08-02", "expert", "password")

    var authenticatedProfile1: ProfileDTO? = null
    var authenticatedProfile2: ProfileDTO? = null

    @BeforeEach
    fun populateDB() {
        authenticatedProfile1 = authService.signup(user1)
        authenticatedProfile2 = authService.createManager(user2)
        authenticatedProfile1 = authService.login(AuthDTO(user1.email, user1.password!!))
        authenticatedProfile2 = authService.login(AuthDTO(user2.email, user2.password!!))
    }

    @AfterEach
    fun deleteDB() {
        authService.deleteProfile(user1.email)
        authService.deleteProfile(user2.email)
        authService.deleteProfile(user3.email)
    }

    @Test
    fun userLoginTest() {
        val authDTO = AuthDTO(user1.email, user1.password!!)
        val headers = HttpHeaders()
        val requestEntity= HttpEntity<AuthDTO>(authDTO, headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/public/login",
            HttpMethod.POST,
            requestEntity,
            ProfileDTO::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
       Assertions.assertTrue { response.body?.token != ""}
    }

    @Test
    fun userLogouTest() {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile1?.token )
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/authenticated/logout",
            HttpMethod.DELETE,
            requestEntity,
            Any::class.java)
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun getUserByEmailTest() {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile1?.token )
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/authenticated/profiles/${user1.email}",
            HttpMethod.GET,
            requestEntity,
            ProfileDTO::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertTrue { response.body?.id == authenticatedProfile1?.id }
        Assertions.assertTrue { response.body?.email == authenticatedProfile1?.email }
    }

    @Test
    fun createUserTest() {
        val headers = HttpHeaders()
        val requestEntity= HttpEntity<ProfileDTO>(user3, headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/public/signup",
            HttpMethod.POST,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
    }

    @Test
    fun modifyUserTest() {
        val profileDTO = profileService.getByEmail(user1.email)
        val newProfileDTO = ProfileDTO(profileDTO.id, profileDTO.email, "newusername", "newname", "newsurname", "1980-06-02", "client")

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile1?.token )

        val requestEntity= HttpEntity<ProfileDTO>(newProfileDTO, headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/authenticated/profiles/${profileDTO.email}",
            HttpMethod.PUT,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
    }

    @Test
    fun signupExpert() {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile2?.token )
        val requestEntity= HttpEntity<ProfileDTO>(user3, headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/manager/createExpert",
            HttpMethod.POST,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
    }

    @Test
    fun getUserByEmailNotExistsTest() {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile1?.token )
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/authenticated/profiles/fakeuser@mail.com",
            HttpMethod.GET,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun profileAlreadyExistsTest() {
        val profileDTO = ProfileDTO(null, "usertest1@mail.com", "newusername", "newname", "newsurname", "1993-10-02", "client")

        val headers = HttpHeaders()
        val requestEntity= HttpEntity<ProfileDTO>(profileDTO, headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/public/signup",
            HttpMethod.POST,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.CONFLICT, response.statusCode)
    }

    @Test
    fun signupExpertForbiddenTest() {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer " + authenticatedProfile1?.token )
        val requestEntity= HttpEntity<ProfileDTO>(user3, headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/manager/createExpert",
            HttpMethod.POST,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.statusCode)
    }

    @Test
    fun getUserByEmailUnauthorized() {
        val headers = HttpHeaders()
        val requestEntity= HttpEntity<Unit>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port/authenticated/profiles/${user1.email}",
            HttpMethod.GET,
            requestEntity,
            Any::class.java)

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
    }




}
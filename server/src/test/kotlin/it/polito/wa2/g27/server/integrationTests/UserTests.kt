package it.polito.wa2.g27.server.integrationTests;

import it.polito.wa2.g27.server.profiles.ProfileDTO
import it.polito.wa2.g27.server.profiles.ProfileRepository
import it.polito.wa2.g27.server.profiles.ProfileService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.sql.DriverManager


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
    @LocalServerPort
    protected var port: Int = 0
    @Autowired
    lateinit var profileService: ProfileService
    @Autowired
    private lateinit var profileRepository: ProfileRepository

    @BeforeEach
    fun populateDB() {
        DriverManager.getConnection(postgres.jdbcUrl, postgres.username, postgres.password).use { conn ->
            val script = File("src/test/resources/schema_populate.sql").readText()
            conn.createStatement().use { stmt ->
                stmt.execute(script)
            }
        }
    }

    @Test
    fun getUserByEmailTest() {
        var expectedProfileDTO = ProfileDTO(null, "ciaone.matano@gmail.com", "frank_m", "Frank", "Matano", "1989-09-14")
        val profileDTO = profileService.getByEmail("ciaone.matano@gmail.com")
        Assertions.assertEquals(expectedProfileDTO, profileDTO)
    }

    @Test
    fun createUserTest() {
        val profileDTO = ProfileDTO(null, "marco.bay@gmail.com", "marco_seaside", "Marco", "Bay", "1999-10-02")
        profileService.createProfile(profileDTO)

        val newProfileDTO = profileService.getByEmail("marco.bay@gmail.com")
        Assertions.assertEquals(profileDTO, newProfileDTO)
    }

    @Test
    fun modifyUserTest() {
        val profileDTO = profileService.getByEmail("ciaone.matano@gmail.com")
        val newProfileDTO = ProfileDTO(profileDTO.id, profileDTO.email, "marco_seaside", "Marco", "Bay", "1999-10-02")

        profileService.modifyProfile(profileDTO.email, newProfileDTO)

        val modifiedProfileDTO = profileService.getByEmail(profileDTO.email)
        Assertions.assertEquals(newProfileDTO, modifiedProfileDTO)
    }
}

/*

//Write here your integration test
val testRestTemplate = TestRestTemplate()
val response: ResponseEntity<String> = testRestTemplate.getForEntity<String>(
    "http://localhost:8080/profiles/pollo2.matano@gmail.com",
    String
)

 */
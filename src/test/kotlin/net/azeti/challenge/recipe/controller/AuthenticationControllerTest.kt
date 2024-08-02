package net.azeti.challenge.recipe.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import net.azeti.challenge.recipe.config.TestConfig
import net.azeti.challenge.recipe.user.UserManagement
import net.azeti.challenge.recipe.user.model.Login
import net.azeti.challenge.recipe.user.model.Registration
import net.azeti.challenge.recipe.user.model.RegistrationResult
import net.azeti.challenge.recipe.user.model.Token
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@Import(TestConfig::class)
@WebMvcTest(AuthenticationController::class)
class AuthenticationControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var authService: UserManagement

    private val objectMapper = jacksonObjectMapper()

    @BeforeEach
    fun setUp() {
        val registrationResult = RegistrationResult("testUser", "test@example.com", "User registered successfully")
        `when`(authService.register(any())).thenReturn(registrationResult)

        val token = Token("dummy-jwt-token")
        `when`(authService.login(any())).thenReturn(token)
    }


    @Test
    fun `should return bad request for null username during registration`() {
        val registration = Registration(
            email = "test@example.com",
            username = null,
            password = "password123"
        )

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registration))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return bad request for null password during registration`() {
        val registration = Registration(
            email = "test@example.com",
            username = "testuser",
            password = null
        )

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registration))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return badrequest for null username during login`() {
        val login = Login(
            username = null,
            password = "password123"
        )

        mockMvc.perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return bad request for null password during login`() {
        val login = Login(
            username = "testuser",
            password = null
        )

        mockMvc.perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return bad request for invalid email`() {
        val registration = Registration(
            email = "invalid-email",
            username = "testuser",
            password = "password123"
        )

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registration))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should register user successfully`() {
        val registration = Registration(
            email = "test@example.com",
            username = "testuser",
            password = "password123"
        )

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registration))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.username").value("testUser"))
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.details").value("User registered successfully"))
    }

    @Test
    fun `should return bad request for invalid email during registration`() {
        val registration = Registration(
            email = "invalid-email",
            username = "testuser",
            password = "password123"
        )

        mockMvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registration))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should authenticate user successfully`() {
        val login = Login(
            username = "testuser",
            password = "password123"
        )

        mockMvc.perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.accessToken").value("dummy-jwt-token"))
    }

    @Test
    fun `should return unauthorized for invalid credentials`() {
        val login = Login(
            username = "invaliduser",
            password = "invalidpassword"
        )

        `when`(authService.login(any())).thenThrow(BadCredentialsException("Invalid credentials"))

        mockMvc.perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login))
        )
            .andExpect(status().isUnauthorized)
    }
}
package net.azeti.challenge.recipe.user.service

import net.azeti.challenge.recipe.security.JwtService
import net.azeti.challenge.recipe.user.UserRepository
import net.azeti.challenge.recipe.user.model.Registration
import net.azeti.challenge.recipe.user.model.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockitoExtension::class)
class UserManagementTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var jwtService: JwtService

    @Mock
    private lateinit var authenticationManager: AuthenticationManager

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @InjectMocks
    private lateinit var userManagement: UserManagementImpl


    @Test
    fun `test registerUser successfully`() {
        val user = User(username = "newuser", email = "newuser@example.com")
        `when`(userRepository.save(any())).thenReturn(user)

        val registeredUser = userManagement.register(Registration("newuser", "newuser@example.com"))

        assertNotNull(registeredUser)
        assertEquals("newuser", registeredUser.username)
        assertEquals("newuser@example.com", registeredUser.email)
    }


}
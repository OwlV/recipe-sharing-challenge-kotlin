import net.azeti.challenge.recipe.user.UserRepository
import net.azeti.challenge.recipe.user.model.User
import net.azeti.challenge.recipe.user.service.UserService
import net.azeti.challenge.recipe.user.service.UserServiceImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserServiceImpl

    @Test
    fun `test findUserByUsername`() {
        val user = User(username = "testuser", email = "testuser@example.com")
        `when`(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user))

        val foundUser = userService.loadUserByUsername("testuser") as User

        assertNotNull(foundUser)
        assertEquals("testuser", foundUser.username)
        assertEquals("testuser@example.com", foundUser.email)
    }

    @Test
    fun `test user not exists`() {
        val user = User(username = "testuser", email = "testuser@example.com")
        `when`(userRepository.findByUsername("testuser2")).thenReturn(Optional.empty())

        assertThrows<UsernameNotFoundException> {
            userService.loadUserByUsername("testuser2") as User
        }

    }

}
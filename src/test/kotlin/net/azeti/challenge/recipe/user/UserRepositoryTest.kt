package net.azeti.challenge.recipe.user

import net.azeti.challenge.recipe.user.model.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@DataJpaTest
@ExtendWith(SpringExtension::class)
class UserRepositoryTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun clear() {
        userRepository.deleteAll()
    }

    @Test
    fun `test findByUsername`() {
        val user = User(username = "testuser", email = "testuser@example.com", password = "pass")
        userRepository.save(user)

        val foundUser = userRepository.findByUsername("testuser")

        assertTrue(foundUser.isPresent)
        assertEquals("testuser", foundUser.get().username)
        assertEquals("testuser@example.com", foundUser.get().email)
    }


    @Test
    fun `test saveUser`() {
        val user = User(username = "newuser", email = "newuser@example.com", password = "pass")
        val savedUser = userRepository.save(user)

        val foundUser = userRepository.findById(savedUser.id!!)

        assertTrue(foundUser.isPresent)
        assertEquals("newuser", foundUser.get().username)
        assertEquals("newuser@example.com", foundUser.get().email)
    }
}
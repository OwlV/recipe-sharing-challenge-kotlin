package net.azeti.challenge.recipe.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.impl.DefaultJwtParserBuilder
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import net.azeti.challenge.recipe.config.properties.JwtConfigurationProperties
import net.azeti.challenge.recipe.user.model.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.Date

@ExtendWith(SpringExtension::class)
@SpringBootTest
class JwtServiceTest {

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var jwtProperties: JwtConfigurationProperties


    @Test
    fun `should generate valid JWT token`() {
        val username = "testUser"
        val token = jwtService.generateToken(User(username = username, password = "somepass"))
        val keyBytes = Decoders.BASE64.decode(jwtProperties.securityKey)

        val claims = DefaultJwtParserBuilder()
            .verifyWith(Keys.hmacShaKeyFor(keyBytes))
            .build()
            .parseSignedClaims(token)
            .payload

        assertEquals(username, claims.subject)
        assertTrue(claims.expiration.after(Date()))
    }

    @Test
    fun `should validate JWT token successfully`() {
        val username = "testUser"
        val userDetails = User(username = username, password = "somepass")
        val token = jwtService.generateToken(userDetails)

        val isValid = jwtService.isTokenValid(token, userDetails)
        assertTrue(isValid)
    }

    @Test
    fun `should not validate JWT token with wrong username`() {
        val username = "testUser"
        val userDetails = User(username = username, password = "somepass")
        val token = jwtService.generateToken(userDetails)

        val isValid = jwtService.isTokenValid(token, User(username = "wrongUser"))
        assertFalse(isValid)
    }

    @Test
    fun `should not validate expired JWT token`() {
        val keyBytes = Decoders.BASE64.decode(jwtProperties.securityKey)
        val expiredToken = Jwts
            .builder()
            .subject("testUser")
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() - 1000))
            .signWith(Keys.hmacShaKeyFor(keyBytes))
            .compact()


        assertThrows<ExpiredJwtException>{
            jwtService.isTokenValid(expiredToken, userDetails = User(username = "testUser"))
        }
    }
}

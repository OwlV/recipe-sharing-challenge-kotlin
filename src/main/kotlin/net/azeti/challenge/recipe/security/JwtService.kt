package net.azeti.challenge.recipe.security

import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseCookie
import org.springframework.web.util.WebUtils
import java.security.Key


class JwtUtils(val jwtSecret: String, val jwtExpirationMs: Int, val jwtCookie: String) {


    fun getJwtFromCookies(request: HttpServletRequest?): String? {
        val cookie: Cookie? = WebUtils.getCookie(request!!, jwtCookie)
        return if (cookie != null) {
            cookie.getValue()
        } else {
            null
        }
    }

    fun generateJwtCookie(userPrincipal: UserDetailsImpl): ResponseCookie {
        val jwt = generateTokenFromUsername(userPrincipal.getUsername())
        val cookie =
            ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge((24 * 60 * 60).toLong()).httpOnly(true).build()
        return cookie
    }

    fun getCleanJwtCookie(): ResponseCookie {
        val cookie = ResponseCookie.from(jwtCookie, null).path("/api").build()
        return cookie
    }

    fun getUserNameFromJwtToken(token: String?): String {
        return Jwts.builder().setSigningKey(key()).build()
            .parseClaimsJws(token).getBody().getSubject()
    }

    private fun key(): Key {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))
    }

    fun validateJwtToken(authToken: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken)
            return true
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token: {}", e.getMessage())
        } catch (e: ExpiredJwtException) {
            logger.error("JWT token is expired: {}", e.getMessage())
        } catch (e: UnsupportedJwtException) {
            logger.error("JWT token is unsupported: {}", e.getMessage())
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: {}", e.message)
        }

        return false
    }

    fun generateTokenFromUsername(username: String?): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().getTime() + jwtExpirationMs))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact()
    }

    companion object {
        @JvmStatic val logger = LoggerFactory.getLogger(JwtUtils::class.java)
    }
}
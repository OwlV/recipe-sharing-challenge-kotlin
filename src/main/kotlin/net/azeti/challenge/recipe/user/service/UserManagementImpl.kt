package net.azeti.challenge.recipe.user.service

import net.azeti.challenge.recipe.security.JwtService
import net.azeti.challenge.recipe.user.UserManagement
import net.azeti.challenge.recipe.user.UserRepository
import net.azeti.challenge.recipe.user.model.Login
import net.azeti.challenge.recipe.user.model.Registration
import net.azeti.challenge.recipe.user.model.RegistrationResult
import net.azeti.challenge.recipe.user.model.Token
import net.azeti.challenge.recipe.user.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserManagementImpl(
    @Autowired val userRepository: UserRepository,
    @Autowired val jwtService: JwtService,
    @Autowired val authenticationManager: AuthenticationManager,
    @Autowired val passwordEncoder: PasswordEncoder
) : UserManagement {
    override fun register(registration: Registration): RegistrationResult {
        val user =
            userRepository.save(
                User( username = registration.username,
                    email = registration.email,
                    password = passwordEncoder.encode(registration.password)
                )
            )
        return RegistrationResult(user.username, user.email, "Successfully registered")
    }

    override fun login(login: Login): Token {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(login.username, login.password) )
        val user = userRepository.findByUsername(login.username!!)
            .orElseThrow {
                BadCredentialsException("Invalid email or password")
            }
        val jwt = jwtService.generateToken(user)
        return Token(jwt, jwtService.extractExpiration(jwt))
    }

}
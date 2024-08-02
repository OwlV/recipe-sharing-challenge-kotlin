package net.azeti.challenge.recipe.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

interface UserService : UserDetailsService {
}

@Service
class UserServiceImpl(@Autowired val userRepository: UserRepository) : UserService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) throw BadCredentialsException("Username is null")
        return userRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("User not found") }
    }
}
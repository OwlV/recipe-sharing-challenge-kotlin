package net.azeti.challenge.recipe.config

import net.azeti.challenge.recipe.config.properties.JwtConfigurationProperties
import net.azeti.challenge.recipe.security.JwtService
import net.azeti.challenge.recipe.security.JwtTokenFilter
import net.azeti.challenge.recipe.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtConfigurationProperties::class)
class SecurityConfiguration(
    @Autowired val userService: UserService,
    @Autowired val jwtService: JwtService
) {
    @Bean
    fun securityFilterChain(
        http: HttpSecurity
    ): SecurityFilterChain = http.csrf { it.disable() } // We disabled csrf for simplicity.
        .authorizeHttpRequests {
            it.requestMatchers("/auth/**", "/api-docs/**", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
        }
        .sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(
            JwtTokenFilter(jwtService, userService),
            UsernamePasswordAuthenticationFilter::class.java
        )
        .build()

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()


    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userService)
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    @Bean
    fun securityEvaluationContextExtension(): SecurityEvaluationContextExtension {
        return SecurityEvaluationContextExtension()
    }
}
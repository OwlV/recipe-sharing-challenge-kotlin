package net.azeti.challenge.recipe.config

import net.azeti.challenge.recipe.config.properties.JwtConfigurationProperties
import net.azeti.challenge.recipe.security.JwtService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtConfigurationProperties::class)
class TestConfig {
    @Bean
    @Primary
    fun testSecurityFilterChain(
        http: HttpSecurity
    ): SecurityFilterChain = http.csrf { it.disable() } // We disabled csrf for simplicity.
        .authorizeHttpRequests {
            it.requestMatchers("/auth/**", "/api-docs/**", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
        }
        .sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .build()

    @Bean
    fun jwtService(jwtConfigurationProperties: JwtConfigurationProperties) = JwtService(jwtConfigurationProperties)
}
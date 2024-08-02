package net.azeti.challenge.recipe.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "security.jwt")
class JwtConfigurationProperties @ConstructorBinding constructor(
    val securityKey: String,
    val expirationTime: Long
)
package net.azeti.challenge.recipe.user.model

import jakarta.validation.constraints.NotBlank
import java.io.Serializable

data class Login(
    @field:NotBlank val username: String? = null,
    @field:NotBlank val password: String? = null
) : Serializable
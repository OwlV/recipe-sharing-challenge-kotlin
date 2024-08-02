package net.azeti.challenge.recipe.user.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import java.io.Serializable


data class Registration(
    @field:Email val email: String? = null,
    @field:NotNull val username: String? = null,
    @field:NotNull val password: String? = null
) : Serializable

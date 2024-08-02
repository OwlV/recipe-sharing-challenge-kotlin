package net.azeti.challenge.recipe.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import java.io.Serializable


data class Registration(
    @Email val email: String? = null,
    @NotNull val username: String? = null,
    @NotNull val password: String? = null
) : Serializable

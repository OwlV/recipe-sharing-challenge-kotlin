package net.azeti.challenge.recipe.user

import java.io.Serializable

data class Login(
    val username: String? = null,
    val password: String? = null
) : Serializable
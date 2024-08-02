package net.azeti.challenge.recipe.user.model

import java.io.Serializable
import java.util.Date

data class Token(
    val accessToken: String,
    val expiresAt: Date? = null
) : Serializable

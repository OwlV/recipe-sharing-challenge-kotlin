package net.azeti.challenge.recipe.user

import java.io.Serializable
import java.util.Date

data class Token(
    val accessToken: String,
    val expiresAt: Date?
) : Serializable

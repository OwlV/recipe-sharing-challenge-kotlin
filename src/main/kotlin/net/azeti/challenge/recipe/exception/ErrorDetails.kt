package net.azeti.challenge.recipe.exception

import java.time.LocalDateTime

class ErrorDetails(
    val errorMessage: String? = null,
    val errorTimestamp: LocalDateTime? = LocalDateTime.now()
)
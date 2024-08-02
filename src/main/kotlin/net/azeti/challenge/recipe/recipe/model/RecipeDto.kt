package net.azeti.challenge.recipe.recipe.model

import java.io.Serializable

data class RecipeDto(
        val id: Long? = null,
        val title: String? = null,
        val username: String? = null,
        val description: String? = null,
        val ingredients: String? = null,
        val instructions: String? = null,
        val serving: Int? = null
) : Serializable
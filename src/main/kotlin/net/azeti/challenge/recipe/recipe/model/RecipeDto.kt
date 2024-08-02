package net.azeti.challenge.recipe.recipe.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.io.Serializable

data class RecipeDto(
    val id: Long? = null,
    @field:NotBlank val title: String? = null,
    val username: String? = null,
    @field:NotBlank val description: String? = null,
    @field:NotBlank val ingredients: String? = null,
    @field:NotBlank val instructions: String? = null,
    @field:Positive val serving: Int? = null
) : Serializable
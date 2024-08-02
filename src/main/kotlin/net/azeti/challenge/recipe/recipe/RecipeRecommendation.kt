package net.azeti.challenge.recipe.recipe

import net.azeti.challenge.recipe.recipe.model.Recipe

interface RecipeRecommendation {
    fun getRecommendedRecipe(): Recipe?
}
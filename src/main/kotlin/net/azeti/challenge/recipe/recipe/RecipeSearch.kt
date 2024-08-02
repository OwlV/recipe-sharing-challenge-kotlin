package net.azeti.challenge.recipe.recipe

import net.azeti.challenge.recipe.recipe.model.Recipe


interface RecipeSearch {

    fun recipesByUsername(usernameValue: String?): List<Recipe>

    fun recipesByTitle(titleValue: String?): List<Recipe>

    fun recipesWithoutBaking(): List<Recipe>

    fun recipesWithoutFrozenIngredients(): List<Recipe>
}
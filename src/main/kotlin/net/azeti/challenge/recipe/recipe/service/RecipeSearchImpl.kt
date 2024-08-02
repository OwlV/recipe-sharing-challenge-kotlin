package net.azeti.challenge.recipe.recipe

import net.azeti.challenge.recipe.recipe.model.Recipe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RecipeSearchImpl(@Autowired val recipeRepository: RecipeRepository) : RecipeSearch {
    override fun recipesByUsername(usernameValue: String?): List<Recipe> {
        return usernameValue?.let { recipeRepository.findByUsernameContainingIgnoreCase(it) }.orEmpty()
    }

    override fun recipesByTitle(titleValue: String?): List<Recipe> {
        return titleValue?.let { recipeRepository.findByTitleContainingIgnoreCase(it) }.orEmpty()
    }

    override fun recipesWithoutBaking(): List<Recipe> {
        return recipeRepository.findByInstructionsNotContainingIgnoreCase("bake")
    }

    override fun recipesWithoutFrozenIngredients(): List<Recipe> {
        return recipeRepository.findByInstructionsNotContainingIgnoreCase("frozen")
    }

}
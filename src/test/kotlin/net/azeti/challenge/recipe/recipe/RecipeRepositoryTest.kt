package net.azeti.challenge.recipe.recipe

import net.azeti.challenge.recipe.createRecipe
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class RecipeRepositoryRest {
    @Autowired lateinit var recipeRepository: RecipeRepository

    @Test
    fun testCreateRecipe() {
        val recipe = createRecipe()
        recipeRepository.save(recipe)
        Assertions.assertNotNull(recipeRepository.findById(recipe.id!!))
    }

    @Test
    fun testUpdateRecipe() {
        val recipe = createRecipe()
        recipeRepository.save(recipe)
        recipe.title = "New title"
        recipeRepository.save(recipe)
        recipeRepository.findById(recipe.id!!).ifPresent {
            assertEquals(recipe.title, it.title)
        }
    }

    @Test
    fun testDeleteRecipe() {

    }
}
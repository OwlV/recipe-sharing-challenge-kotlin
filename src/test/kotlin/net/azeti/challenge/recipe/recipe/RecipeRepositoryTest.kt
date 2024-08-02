package net.azeti.challenge.recipe.recipe

import net.azeti.challenge.recipe.recipe.model.Ingredient
import net.azeti.challenge.recipe.recipe.model.Recipe
import net.azeti.challenge.recipe.recipe.model.Unit
import net.azeti.challenge.recipe.user.model.User
import net.azeti.challenge.recipe.user.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles


@DataJpaTest
class RecipeRepositoryTest {
    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var user: User

    @BeforeEach
    fun setUp() {
        // Setup initial data
        recipeRepository.deleteAll()
        userRepository.deleteAll()
        user = userRepository.save(User(username = "testuser", password = "password", email = "test@mail.com"))
    }

    @Test
    fun `should not save recipe with no ingredients`() {
        val recipe = Recipe(
            title = "Test Recipe",
            user = user,
            description = "Test Description",
            instructions = "Test Instructions",
            serving = 4
        )

        assertThrows<Exception> {
            recipeRepository.save(recipe)
        }
    }

    @Test
    fun `should save recipe with at least one ingredient`() {
        val ingredient = Ingredient(
            recipe = null, // Will be set later
            amount = 100.0,
            unit = Unit.GRAM,
            type = "Flour"
        )

        val recipe = Recipe(
            title = "Test Recipe",
            user = user,
            description = "Test Description",
            instructions = "Test Instructions",
            serving = 4,
            ingredients = listOf(ingredient)
        )

        // Set the recipe reference in the ingredient
        ingredient.recipe = recipe

        val savedRecipe = recipeRepository.save(recipe)
        assertNotNull(savedRecipe.id)
        assertTrue(savedRecipe.ingredients.isNotEmpty())
    }

    @Test
    fun `should find recipes by title containing`() {
        val ingredient = Ingredient(
            recipe = null, // Will be set later
            amount = 100.0,
            unit = Unit.GRAM,
            type = "Flour"
        )

        val recipe = Recipe(
            title = "Unique Title",
            user = user,
            description = "Test Description",
            instructions = "Test Instructions",
            serving = 4,
            ingredients = listOf(ingredient)
        )

        ingredient.recipe = recipe
        recipeRepository.save(recipe)

        var foundRecipes = recipeRepository.findByTitleContainingIgnoreCase("Unique Title")
        assertEquals(1, foundRecipes.size)
        assertEquals("Unique Title", foundRecipes[0].title)

        foundRecipes = recipeRepository.findByTitleContainingIgnoreCase("Unique title")
        assertEquals(1, foundRecipes.size)
        assertEquals("Unique Title", foundRecipes[0].title)
    }

    @Test
    fun `should find recipes by author containing`() {
        val ingredient = Ingredient(
            recipe = null, // Will be set later
            amount = 100.0,
            unit = Unit.GRAM,
            type = "Flour"
        )

        val recipe = Recipe(
            title = "Unique Title",
            user = user,
            description = "Test Description",
            instructions = "Test Instructions",
            serving = 4,
            ingredients = listOf(ingredient)
        )

        ingredient.recipe = recipe
        recipeRepository.save(recipe)

        var foundRecipes = recipeRepository.findByUsernameContainingIgnoreCase("testuser")
        assertEquals(1, foundRecipes.size)
        assertEquals("testuser", foundRecipes[0].user?.username)

        foundRecipes = recipeRepository.findByUsernameContainingIgnoreCase("User")
        assertEquals(1, foundRecipes.size)
        assertEquals("testuser", foundRecipes[0].user?.username)
    }


    @Test
    fun `should find recipes by instructions not containing`() {
        val ingredient = Ingredient(
            recipe = null, // Will be set later
            amount = 100.0,
            unit = Unit.GRAM,
            type = "Flour"
        )

        val recipe = Recipe(
            title = "Unique Title",
            user = user,
            description = "Test Description",
            instructions = "Test Instructions",
            serving = 4,
            ingredients = listOf(ingredient)
        )

        ingredient.recipe = recipe
        recipeRepository.save(recipe)

        var foundRecipes = recipeRepository.findByInstructionsNotContainingIgnoreCase("instructions")
        assertEquals(0, foundRecipes.size)

        foundRecipes = recipeRepository.findByInstructionsNotContainingIgnoreCase("actual")
        assertEquals(1, foundRecipes.size)
        assertEquals("Test Instructions", foundRecipes[0].instructions)
    }

    @Test
    fun `should find recipes by ingredients not containing`() {
        val ingredient = Ingredient(
            recipe = null, // Will be set later
            amount = 100.0,
            unit = Unit.GRAM,
            type = "Flour"
        )

        val recipe = Recipe(
            title = "Unique Title",
            user = user,
            description = "Test Description",
            instructions = "Test Instructions",
            serving = 4,
            ingredients = listOf(ingredient)
        )

        ingredient.recipe = recipe
        recipeRepository.save(recipe)

        var foundRecipes = recipeRepository.findByIngredientsNotContaining("Eggs")
        assertEquals(1, foundRecipes.size)
        assertEquals("Flour", foundRecipes[0].ingredients.firstOrNull()?.type)

        foundRecipes = recipeRepository.findByIngredientsNotContaining("flour")
        assertEquals(0, foundRecipes.size)
    }
}
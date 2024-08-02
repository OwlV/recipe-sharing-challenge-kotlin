package net.azeti.challenge.recipe.controller

import net.azeti.challenge.recipe.recipe.RecipeManagement
import net.azeti.challenge.recipe.recipe.RecipeRecommendation
import net.azeti.challenge.recipe.recipe.RecipeSearch
import net.azeti.challenge.recipe.recipe.model.Ingredient
import net.azeti.challenge.recipe.recipe.model.Recipe
import net.azeti.challenge.recipe.user.User
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class RecipeManagementControllerTest {
    @Mock
    lateinit var recipeManagement: RecipeManagement

    @InjectMocks
    lateinit var recipeManagementController: RecipeManagementController

    @ParameterizedTest
    @CsvSource(textBlock = """
        testUser,true
        other,false""", nullValues = ["null"])
    fun testSearchByUsername(searchString: String, resultExists: Boolean) {
        val recipe = Optional.of(createRecipe())
        Mockito.`when`(recipeManagement.getById(1)).thenReturn(recipe)
        val found = this.recipeManagementController.getRecipe(1)
        Mockito.verify(recipeManagement, Mockito.times(1)).getById(1)
        Assertions.assertEquals(resultExists, found == null)
    }


    fun createRecipe(id: Long? = 1, title: String? = "awesome recipe", username: String? = "testUser",
                     description: String? = null, ingredients: Collection<Ingredient> = emptyList(), instructions: String? = "cook till ready",
                     servings: Int? = null) = Recipe(id, title, User(username = username), description, ingredients, instructions, servings)

    companion object {
        @JvmStatic
        @BeforeAll
        fun init() {
            MockitoAnnotations.openMocks(RecipeManagementControllerTest::class)
        }
    }
}
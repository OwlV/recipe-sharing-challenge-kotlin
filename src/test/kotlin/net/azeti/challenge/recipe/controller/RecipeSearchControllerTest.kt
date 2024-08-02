package net.azeti.challenge.recipe.controller

import net.azeti.challenge.recipe.recipe.RecipeRecommendation
import net.azeti.challenge.recipe.recipe.RecipeSearch
import net.azeti.challenge.recipe.recipe.model.Recipe
import net.azeti.challenge.recipe.recipe.model.RecipeModelMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@WebMvcTest(RecipeSearchController::class)
class RecipeSearchControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var recipeSearch: RecipeSearch

    @MockBean
    private lateinit var recommendation: RecipeRecommendation

    private val mapper = RecipeModelMapper()

    @Test
    @WithMockUser(username = "testUser")
    fun `should return recipes by title`() {
        val recipes = listOf(
            Recipe(id = 1L, title = "Test Recipe", description = "Test Description", ingredients = mutableListOf(), instructions = "Test Instructions", serving = 4)
        )
        `when`(recipeSearch.recipesByTitle(anyString())).thenReturn(recipes)

        mockMvc.perform(get("/api/recipe/search/byTitle")
            .param("title", "Test")
            .accept(org.springframework.http.MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].title").value("Test Recipe"))

        verify(recipeSearch).recipesByTitle(anyString())
    }

    @Test
    @WithMockUser(username = "testUser")
    fun `should return recipes by author`() {
        val recipes = listOf(
            Recipe(id = 1L, title = "Test Recipe", description = "Test Description", ingredients = mutableListOf(), instructions = "Test Instructions", serving = 4)
        )
        `when`(recipeSearch.recipesByUsername(anyString())).thenReturn(recipes)

        mockMvc.perform(get("/api/recipe/search/byAuthor")
            .param("author", "testUser")
            .accept(org.springframework.http.MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].title").value("Test Recipe"))

        verify(recipeSearch).recipesByUsername(anyString())
    }

    @Test
    @WithMockUser(username = "testUser")
    fun `should return recommended recipe`() {
        val recipe = Recipe(id = 1L, title = "Recommended Recipe", description = "Recommended Description", ingredients = mutableListOf(), instructions = "Recommended Instructions", serving = 4)
        `when`(recommendation.getRecommendedRecipe()).thenReturn(recipe)

        mockMvc.perform(get("/api/recipe/search/recommended")
            .accept(org.springframework.http.MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("Recommended Recipe"))

        verify(recommendation).getRecommendedRecipe()
    }

    @Test
    @WithMockUser(username = "testUser")
    fun `should return 404 when no recommended recipe found`() {
        `when`(recommendation.getRecommendedRecipe()).thenReturn(null)

        mockMvc.perform(get("/api/recipe/search/recommended")
            .accept(org.springframework.http.MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)

        verify(recommendation).getRecommendedRecipe()
    }
}
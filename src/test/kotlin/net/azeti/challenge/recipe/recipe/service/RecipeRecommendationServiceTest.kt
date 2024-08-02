package net.azeti.challenge.recipe.recipe.service

import net.azeti.challenge.recipe.recipe.RecipeRepository
import net.azeti.challenge.recipe.recipe.model.Recipe
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.any
import org.mockito.Mockito.anyMap
import org.mockito.Mockito.anyString
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestTemplate

@ExtendWith(SpringExtension::class)
@SpringBootTest
class RecipeRecommendationServiceTest {

    @MockBean
    private lateinit var restTemplate: RestTemplate

    @Autowired
    private lateinit var recommendationService: RecipeRecommendationService

    @MockBean
    private lateinit var recipeRepository: RecipeRepository

    @Test
    fun `should return non-baking recipes when temperature is above threshold`() {
        `when`(restTemplate.getForEntity(anyString(), any(Class::class.java), anyMap<String, String>()))
            .thenReturn(ResponseEntity.ok(WeatherApiResponse(listOf(CurrentConditions(30.0)))))

        `when`(recipeRepository.findByInstructionsNotContainingIgnoreCase("bake")).thenReturn(
            listOf(
                Recipe(id = 1L, title = "Salad", instructions = "Mix ingredients"),
                Recipe(id = 2L, title = "Smoothie", instructions = "Blend ingredients")
            )
        )

        val recommendation = recommendationService.getRecommendedRecipe()
        assertNotNull(recommendation)
    }

    @Test
    fun `should return non-frozen recipes when temperature is below threshold`() {
        `when`(restTemplate.getForEntity(anyString(), any(Class::class.java), anyMap<String, String>()))
            .thenReturn(ResponseEntity.ok(WeatherApiResponse(listOf(CurrentConditions(0.0)))))
        `when`(recipeRepository.findByIngredientsNotContaining("frozen")).thenReturn(
            listOf(
                Recipe(id = 1L, title = "Soup", instructions = "Heat ingredients"),
                Recipe(id = 2L, title = "Stew", instructions = "Cook ingredients")
            )
        )

        val recommendation = recommendationService.getRecommendedRecipe()
        assertNotNull(recommendation)
    }

    @Test
    fun `should return random recipe when no limitations`() {
        `when`(restTemplate.getForEntity(anyString(), any(Class::class.java), anyMap<String, String>()))
            .thenReturn(ResponseEntity.ok(WeatherApiResponse(listOf(CurrentConditions(15.0)))))
        `when`(recipeRepository.findAll()).thenReturn(
            listOf(
                Recipe(id = 1L, title = "Soup", instructions = "Bake ingredients"),
                Recipe(id = 2L, title = "Stew", instructions = "Frozen ingredients")
            )
        )

        val recommendation = recommendationService.getRecommendedRecipe()
        assertNotNull(recommendation)
    }
}

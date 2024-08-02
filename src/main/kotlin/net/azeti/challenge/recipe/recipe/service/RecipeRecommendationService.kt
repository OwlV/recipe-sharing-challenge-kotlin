package net.azeti.challenge.recipe.recipe

import net.azeti.challenge.recipe.exception.NoSuchRecipeException
import net.azeti.challenge.recipe.recipe.model.Ingredient
import net.azeti.challenge.recipe.recipe.model.Recipe
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@Service
class RecipeRecommendationService(
    private val recipeRepository: RecipeRepository,
    private val weatherServiceUrlTemplate: String,
    private val noBakingTemp: Int,
    private val noFrozenTemp: Int
) : RecipeRecommendation {
    override fun getRecommendedRecipe(): Recipe {
        val currentTemperature = fetchCurrentTemp()
        val recommended = when {
            currentTemperature == null -> recipeRepository.findAll()
            currentTemperature > noBakingTemp -> recipeRepository.findByInstructionsNotContainingIgnoreCase("bake")
            currentTemperature < noFrozenTemp -> recipeRepository.findByIngredientsNotContaining("frozen")
            else -> recipeRepository.findAll()
        }.takeIf { it.isNotEmpty() }?.let { it.elementAtOrNull(Random.nextInt(it.count())) }

        return recommended ?: run {
            logger.info("Failed to find recipe recommendation - no recipes found")
            throw NoSuchRecipeException()
        }
    }


    private fun fetchCurrentTemp(): Double? {
        return try {
            return RestTemplate().getForEntity(
                weatherServiceUrlTemplate,
                WeatherApiResponse::class.java,
                mapOf("date-time" to getCurrentDateTime())
            ).body?.currentTemperature()
        } catch (e: HttpClientErrorException) {
            logger.error("Client error when fetching current temperature: ${e.statusCode}", e)
            throw WeatherServiceException("Client error when fetching current temperature", e)
        } catch (e: HttpServerErrorException) {
            logger.error("Server error when fetching current temperature: ${e.statusCode}", e)
            throw WeatherServiceUnavailableException("Server error when fetching current temperature", e)
        } catch (e: ResourceAccessException) {
            logger.error("Resource access error when fetching current temperature", e)
            throw WeatherServiceUnavailableException("Resource access error when fetching current temperature", e)
        } catch (e: RestClientException) {
            logger.error("Error fetching current temperature from Weather Service", e)
            throw WeatherServiceException("Error fetching current temperature from Weather Service", e)
        }
    }

    private fun getCurrentDateTime(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
    }

    companion object {
        @JvmStatic val logger = LoggerFactory.getLogger(RecipeRecommendation::class.java)
    }
}

data class CurrentConditions(val temp: Double) : Serializable

data class WeatherApiResponse(val currentConditions: List<CurrentConditions>) : Serializable {
    fun currentTemperature() = currentConditions.firstOrNull()?.temp
}

open class WeatherServiceException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
class WeatherServiceUnavailableException(message: String, cause: Throwable? = null) : WeatherServiceException(message, cause)

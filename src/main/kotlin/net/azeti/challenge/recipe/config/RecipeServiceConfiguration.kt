package net.azeti.challenge.recipe.config

import net.azeti.challenge.recipe.config.properties.RecommendationServiceConfigurationProperties
import net.azeti.challenge.recipe.recipe.RecipeRecommendation
import net.azeti.challenge.recipe.recipe.RecipeRepository
import net.azeti.challenge.recipe.recipe.service.RecipeRecommendationService
import net.azeti.challenge.recipe.util.HttpClientLoggingInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
@EnableConfigurationProperties(RecommendationServiceConfigurationProperties::class)
class RecipeServiceConfiguration {

    @Bean
    fun restTemplate(): RestTemplate {
        val restTemplate = RestTemplate()
        restTemplate.interceptors.add(HttpClientLoggingInterceptor())
        return restTemplate
    }

    @Bean
    fun recommendationService(
        @Autowired restTemplate: RestTemplate,
        @Autowired recipeRepository: RecipeRepository,
        @Autowired configProperties: RecommendationServiceConfigurationProperties
    ): RecipeRecommendation {
        return RecipeRecommendationService(
            restTemplate,
            recipeRepository,
            configProperties.httpUri,
            configProperties.noBakingTemp,
            configProperties.noFrozenTemp
        )
    }
}
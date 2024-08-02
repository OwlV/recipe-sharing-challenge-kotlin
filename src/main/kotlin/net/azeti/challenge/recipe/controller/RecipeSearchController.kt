package net.azeti.challenge.recipe.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import net.azeti.challenge.recipe.exception.ErrorDetails
import net.azeti.challenge.recipe.recipe.RecipeRecommendation
import net.azeti.challenge.recipe.recipe.RecipeSearch
import net.azeti.challenge.recipe.recipe.model.RecipeDto
import net.azeti.challenge.recipe.recipe.model.RecipeModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/recipe/search")
class RecipeSearchController(
    @Autowired val recipeSearch: RecipeSearch,
    @Autowired val recommendation: RecipeRecommendation
) {

    private val mapper = RecipeModelMapper()

    @Operation(
        summary = "Search recipes by partial or full title match. Requires authentication.",
        security = [SecurityRequirement(name = "Authorized")]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Recipes found",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                array = ArraySchema(schema = Schema(implementation = RecipeDto::class))
            )]
        ),
        ApiResponse(
            responseCode = "401",
            description = "User is not authorized",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = ErrorDetails::class)
            )]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Matched recipes not found",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = ErrorDetails::class)
            )]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = ErrorDetails::class)
            )]
        )
    )
    @GetMapping("/byTitle")
    fun getRecipesByTitle(@RequestParam("title") title: String): List<RecipeDto> {
        return recipeSearch.recipesByTitle(title).map { mapper.entityToDto(it) }
    }

    @Operation(
        summary = "Search recipes by partial or full username (author) match. Requires authentication.",
        security = [SecurityRequirement(name = "Authorized")]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Recipes found",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                array = ArraySchema(schema = Schema(implementation = RecipeDto::class))
            )]
        ),
        ApiResponse(
            responseCode = "401",
            description = "User is not authorized",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = ErrorDetails::class)
            )]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Matched recipes not found",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = ErrorDetails::class)
            )]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = ErrorDetails::class)
            )]
        )
    )
    @GetMapping("/byAuthor")
    fun getRecipesByAuthor(@RequestParam("author") username: String): List<RecipeDto> {
        return recipeSearch.recipesByUsername(username).map { mapper.entityToDto(it) }
    }


    @Operation(
        summary = "Get recommended recipe depending on current weather in Berlin. " +
                "Avoids baking when it's too hot and frozen ingredients when it's too cold. Requires authentication.",
        security = [SecurityRequirement(name = "Authorized")]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Recommended recipe found",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = RecipeDto::class)
            )]
        ),
        ApiResponse(
            responseCode = "401",
            description = "User is not authorized",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = ErrorDetails::class)
            )]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Matched recipes not found",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = ErrorDetails::class)
            )]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = ErrorDetails::class)
            )]
        )
    )
    @GetMapping("/recommended")
    fun getRecipesByAuthor(): ResponseEntity<RecipeDto> {
        return ResponseEntity.ofNullable(recommendation.getRecommendedRecipe()?.let { mapper.entityToDto(it) })
    }
}
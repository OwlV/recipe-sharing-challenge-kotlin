package net.azeti.challenge.recipe.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import net.azeti.challenge.recipe.exception.ErrorDetails
import net.azeti.challenge.recipe.exception.InvalidInputException
import net.azeti.challenge.recipe.recipe.RecipeManagement
import net.azeti.challenge.recipe.recipe.model.RecipeDto
import net.azeti.challenge.recipe.recipe.model.RecipeModelMapper
import net.azeti.challenge.recipe.util.ApiRequestBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/recipe")
class RecipeManagementController(
    @Autowired val recipeManagement: RecipeManagement
) {

    private val mapper = RecipeModelMapper()

    @Operation(
        summary = "Fetch recipe by id. Requires authentication.",
        security = [SecurityRequirement(name = "Authorized")]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Recipe found",
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
    @GetMapping("/{id}")
    fun getRecipe(@PathVariable("id") id: Long): ResponseEntity<RecipeDto> {
        return ResponseEntity.of(recipeManagement.getById(id).map { mapper.entityToDto(it) })
    }


    @Operation(
        summary = "Fetch all recipes created by particular user. Requires authentication.",
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
            description = "Matched recipes not found"
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
    @GetMapping("/byUserName")
    fun getUserRecipes(@RequestParam username: String): List<RecipeDto> {
        return recipeManagement.getByUser(username).map { mapper.entityToDto(it) }
    }


    @Operation(
        summary = "Create new recipe with authorized user as an author. Requires authentication.",
        security = [SecurityRequirement(name = "Authorized")]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Recipe created",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = RecipeDto::class)
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid client request",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = ErrorDetails::class)
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
            responseCode = "500",
            description = "Internal server error",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = ErrorDetails::class)
            )]
        )
    )
    @ApiRequestBody(
        description = "Recipe object",
        content = [Content(schema = Schema(implementation = RecipeDto::class))]
    )
    @PostMapping("/", consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun createRecipe(@RequestBody @Valid recipe: RecipeDto): ResponseEntity<RecipeDto> {
        return ResponseEntity.ok(mapper.dtoToEntity(recipe).let { mapper.entityToDto(recipeManagement.create(it)) })
    }


    @Operation(
        summary = "Update attributes of recipe with given id. Requires authentication.",
        security = [SecurityRequirement(name = "Authorized")]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Recipe updated",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = RecipeDto::class)
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid client request",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = ErrorDetails::class)
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
            responseCode = "403",
            description = "Authorized user is not the author of the recipe",
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
    @ApiRequestBody(
        description = "Recipe object",
        content = [Content(schema = Schema(implementation = RecipeDto::class))]
    )
    @PutMapping("/{id}", consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun updateRecipe(@PathVariable("id") id: Long, @RequestBody @Valid recipe: RecipeDto): ResponseEntity<RecipeDto> {
        if (recipe.id != null && id != recipe.id) throw InvalidInputException("recipe.id should be null or equal to id request param")
        return ResponseEntity.ok(mapper.dtoToEntity(recipe).let { mapper.entityToDto(recipeManagement.update(id, it)) })
    }


    @Operation(
        summary = "Delete the recipe with given id. Requires authentication.",
        security = [SecurityRequirement(name = "Authorized")]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Recipe deleted",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = RecipeDto::class)
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid client request",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = ErrorDetails::class)
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
            responseCode = "403",
            description = "Authorized user is not the author of the recipe",
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
    @DeleteMapping("/{id}")
    fun deleteRecipe(@PathVariable("id") id: Long): ResponseEntity<RecipeDto> {
        return ResponseEntity.ofNullable(recipeManagement.delete(id)?.let { mapper.entityToDto(it) })
    }
}
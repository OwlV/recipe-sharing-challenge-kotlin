package net.azeti.challenge.recipe.exception

open class RecipeManagementException(message: String) : Exception(message)

class NoSuchRecipeException(message: String? = null) :
    RecipeManagementException(message ?: "Failed to find recipe by given parameters")

class RecipeValidationException(message: String) : RecipeManagementException(message)

class NoRecipesCreated(message: String) : RecipeManagementException(message)

class InvalidInputException(message: String) : RecipeManagementException(message)


package net.azeti.challenge.recipe.recipe.model


class RecipeModelMapper {
    fun dtoToEntity(recipeDto: RecipeDto): Recipe {
        val recipe = Recipe(
            id = recipeDto.id,
            title = recipeDto.title,
            description = recipeDto.description,
            instructions = recipeDto.instructions,
            serving = recipeDto.serving
        )
        recipe.ingredientsFromString(recipeDto.ingredients)?.let { recipe.ingredients = it }
        return recipe
    }

    fun entityToDto(recipe: Recipe): RecipeDto {
        return RecipeDto(
            recipe.id,
            recipe.title,
            recipe.user?.username,
            recipe.description,
            recipe.ingredients.joinToString(separator = ", ") { it.toDtoString() },
            recipe.instructions,
            recipe.serving
        )

    }

}

fun Ingredient.toDtoString(): String = "$amount ${unit?.shortName} $type"

fun Recipe.ingredientsFromString(stringRepresentation: String?): Collection<Ingredient>? =
    stringRepresentation?.split(",")?.mapNotNull { part ->
        part.trim().takeIf { it.isNotEmpty() }?.split(" ")?.let { ingredientAsString ->
            Ingredient(
                this, ingredientAsString[0].toDoubleOrNull(),
                ingredientAsString[1].let { shortName -> Unit.parseFromShortName(shortName) }, ingredientAsString[2]
            )
        }
    }
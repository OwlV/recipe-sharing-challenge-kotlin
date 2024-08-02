package net.azeti.challenge.recipe

import net.azeti.challenge.recipe.recipe.model.Ingredient
import net.azeti.challenge.recipe.recipe.model.Recipe
import net.azeti.challenge.recipe.user.model.User

fun createRecipe(
    id: Long? = 1,
    title: String? = "awesome recipe",
    user: User? = createUser(1, "someuser"),
    description: String? = null,
    ingredients: Collection<Ingredient> = emptyList(),
    instructions: String? = "cook till ready",
    servings: Int? = null
) = Recipe(id, title, user, description, ingredients, instructions, servings)

fun createUser(id: Long? = 1, username: String? = "testUser", email: String? = "testEmail") = User(id, username, email)

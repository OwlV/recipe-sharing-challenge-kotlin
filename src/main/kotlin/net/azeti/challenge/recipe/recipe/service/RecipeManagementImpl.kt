package net.azeti.challenge.recipe.recipe.service

import net.azeti.challenge.recipe.exception.RecipeAlreadyExistsException
import net.azeti.challenge.recipe.recipe.RecipeManagement
import net.azeti.challenge.recipe.recipe.RecipeRepository
import net.azeti.challenge.recipe.recipe.model.Recipe
import net.azeti.challenge.recipe.user.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.Optional


@Service
class RecipeManagementImpl(@Autowired val recipeRepository: RecipeRepository) : RecipeManagement {

    override fun create(recipe: Recipe): Recipe {
        if (recipe.id != null && recipeRepository.existsById(recipe.id)) {
            throw RecipeAlreadyExistsException("Recipe with id ${recipe.id} already exists")
        }
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication.isAuthenticated) {
            when (authentication.principal) {
                is User -> recipe.user = authentication.principal as User
                else -> throw InsufficientAuthenticationException("Unrecognizable user credentials")
            }

        } else {
            throw InsufficientAuthenticationException("User not authorized")
        }
        return recipeRepository.save(recipe)
    }

    override fun getById(id: Long): Optional<Recipe> {
        return recipeRepository.findById(id)
    }

    override fun update(id: Long, recipe: Recipe): Recipe {
        return recipeRepository.findById(id).map {
            if (!canEdit(it)) {
                throw AccessDeniedException("Authenticated user is not authorized to delete recipe with id $id")
            }
            it.description = recipe.description
            it.title = recipe.title
            it.ingredients = recipe.ingredients
            it.serving = recipe.serving
            it.instructions = recipe.instructions
            recipe
        }.orElseGet { recipeRepository.save(recipe) }
    }

    override fun delete(id: Long): Recipe? {
        val existing = recipeRepository.findById(id)
        if (existing.isPresent) {
            if (!canEdit(existing.get())) {
                throw AccessDeniedException("Authenticated user is not authorized to delete recipe with id $id")
            }
            recipeRepository.deleteById(id)
            return existing.get()
        }
        return null
    }

    override fun getByUser(username: String): List<Recipe> {
        return recipeRepository.findAllByUsername(username)
    }

    private fun canEdit(recipe: Recipe): Boolean =
        SecurityContextHolder.getContext().authentication?.takeIf { it.isAuthenticated }
            ?.principal?.takeIf { it is User }?.let { (it as User).id == recipe.user?.id }
            ?: false

}
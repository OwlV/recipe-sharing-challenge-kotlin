package net.azeti.challenge.recipe.recipe

import net.azeti.challenge.recipe.recipe.model.Recipe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository : JpaRepository<Recipe, Long> {
    @Query("select r from recipe r where  0 < LOCATE(lower(:username), lower(r.user.username))")
    fun findByUsernameContainingIgnoreCase(@Param("username") username: String): List<Recipe>

    @Query("select r from recipe r where r.user.username = '?1'")
    fun findAllByUsername(username: String): List<Recipe>
    fun findByTitleContainingIgnoreCase(title: String): List<Recipe>
    fun findByInstructionsNotContainingIgnoreCase(instruction: String): List<Recipe>

    @Query("select r from recipe r where not exists (" +
            "select 1 from ingredient i where i.recipe.id = r.id and lower(i.type) like lower(concat('%', :ingredientType, '%')))")
    fun findByIngredientsNotContaining(@Param("ingredientType") ingredientType: String): List<Recipe>
}
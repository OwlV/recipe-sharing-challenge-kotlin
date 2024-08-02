package net.azeti.challenge.recipe.recipe.model

import jakarta.persistence.*
import jakarta.persistence.FetchType.EAGER
import jakarta.persistence.GenerationType.IDENTITY
import net.azeti.challenge.recipe.exception.RecipeValidationException
import net.azeti.challenge.recipe.user.model.User
import java.io.Serializable


@Entity(name = "recipe")
data class Recipe(
    @Id @GeneratedValue(strategy = IDENTITY) val id: Long? = null,
    @Column(nullable = false) var title: String? = null,
    @ManyToOne(fetch = EAGER) @JoinColumn(name = "user_id", nullable = false) var user: User? = null,
    var description: String? = null,
    @OneToMany(fetch = EAGER, mappedBy = "recipe", cascade = [CascadeType.ALL])
    var ingredients: Collection<Ingredient> = emptyList(),
    @Column(nullable = false) var instructions: String? = null,
    var serving: Int? = null
) : Serializable {
    @PrePersist
    @PreUpdate
    fun validateIngredients() {
        if (ingredients.isEmpty()) {
            throw RecipeValidationException("Recipe must have at least one ingredient")
        }
    }
}

@Entity(name = "ingredient")
@IdClass(IngredientId::class)
data class Ingredient(
    @ManyToOne @JoinColumn(name = "recipe_id") @Id var recipe: Recipe? = null,
    val amount: Double? = null,
    @Enumerated(EnumType.STRING) val unit: Unit? = null,
    @Id val type: String? = null
) : Serializable

data class IngredientId(val recipe: Recipe? = null, val type: String? = null) : Serializable

enum class Unit(val shortName: String, val description: String) : Serializable {
    GRAM("g", "Gram"),
    KILOGRAM("kg", "Kilogram"),
    MILLILITER("ml", "Milliliter"),
    LITER("l", "Liter"),
    PIECE("pc", "Piece"),
    TEASPOON("tsp", "Teaspoon"),
    TABLESPOON("tbsp", "Tablespoon"),
    PINCH("pinch", "A dash");

    companion object {
        fun parseFromShortName(shortName: String?): Unit? = shortName?.lowercase()?.let {
            entries.firstOrNull { unit -> shortName.equals(unit.shortName, true) }
        }

        fun parseFromDescription(description: String?): Unit? = description?.lowercase()?.let {
            entries.firstOrNull { unit -> description.equals(unit.description, true) }
        }
    }
}
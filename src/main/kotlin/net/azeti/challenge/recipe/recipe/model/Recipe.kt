package net.azeti.challenge.recipe.recipe.model

import jakarta.persistence.*
import jakarta.persistence.FetchType.EAGER
import jakarta.persistence.GenerationType.IDENTITY
import net.azeti.challenge.recipe.user.User
import java.io.Serializable
import java.util.Collections


@Entity(name = "recipe")
data class Recipe(
        @Id @GeneratedValue(strategy = IDENTITY) val id: Long? = null,
        @Column(nullable = false) var title: String?,
        @ManyToOne(fetch = EAGER) @JoinColumn(name = "user_id") var user: User?,
        var description: String?,
        @OneToMany(fetch = EAGER, mappedBy = "recipe", cascade = [CascadeType.ALL])
        var ingredients: Collection<Ingredient> = Collections.emptyList(),
        @Column(nullable = false) var instructions: String? = null,
        var serving: Int? = null
): Serializable

@Entity(name = "ingredient")
@IdClass(IngredientId::class)
data class Ingredient(
        @ManyToOne @JoinColumn(name = "recipe_id")
        @Id var recipe: Recipe?,
        val amount: Double?,
        @Enumerated(EnumType.STRING)
        val unit: Unit?,
        @Id val type: String?
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
package net.azeti.challenge.recipe.recipe.model

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.io.Serializable
import kotlin.reflect.KClass

data class RecipeDto(
    val id: Long? = null,
    @field:NotBlank val title: String? = null,
    val username: String? = null,
    @field:NotBlank val description: String? = null,
    @field:NotBlank @field:IngredientAsString val ingredients: String? = null,
    @field:NotBlank val instructions: String? = null,
    @field:Positive val serving: Int? = null
) : Serializable

class IngredientStringValidator : ConstraintValidator<IngredientAsString, String> {
    override fun isValid(p0: String?, p1: ConstraintValidatorContext?): Boolean {
        return p0?.matches(Regex("(\\d+(\\.\\d+)?\\s*(g|kg|ml|l|pc|tsp|tbsp|pinch)\\s+[a-zA-Z]+)(,\\s*\\d+(\\.\\d+)?\\s*(g|kg|ml|l|pc|tsp|tbsp|pinch)\\s+[a-zA-Z]+)*")) == true
    }

}

@Target(AnnotationTarget.FIELD)
@MustBeDocumented
@Constraint(validatedBy = [IngredientStringValidator::class])
annotation class IngredientAsString(
    val message: String = "Should match ingredient form: List of comma separated triples (space is used to " +
            "separate values of the trippe) : Double Unit String",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
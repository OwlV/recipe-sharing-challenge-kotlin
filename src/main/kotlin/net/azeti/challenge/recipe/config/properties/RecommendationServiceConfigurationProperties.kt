package net.azeti.challenge.recipe.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.boot.context.properties.bind.Name

@ConfigurationProperties(prefix = "recommendations-service")
class RecommendationServiceConfigurationProperties @ConstructorBinding constructor(
    @Name("http.uri") var httpUri: String,
    @Name("constraints.no-baking-temp") var noBakingTemp: Int,
    @Name("constraints.no-frozen-temp") var noFrozenTemp: Int
)
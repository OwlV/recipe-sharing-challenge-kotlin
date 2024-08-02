package net.azeti.challenge.recipe.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import net.azeti.challenge.recipe.exception.ErrorDetails
import net.azeti.challenge.recipe.user.UserManagement
import net.azeti.challenge.recipe.user.model.Login
import net.azeti.challenge.recipe.user.model.Registration
import net.azeti.challenge.recipe.user.model.RegistrationResult
import net.azeti.challenge.recipe.user.model.Token
import net.azeti.challenge.recipe.util.ApiRequestBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/auth")
@RestController
@Validated
class AuthenticationController(
    @Autowired val authService: UserManagement
) {

    @Operation(summary = "Create user account. Authentication not required")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "User was successfully created",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = RegistrationResult::class)
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid input parameters",
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
        description = "User account details",
        content = [Content(schema = Schema(implementation = Registration::class))]
    )
    @PostMapping("/signup", consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun register(@RequestBody @Valid registration: Registration): ResponseEntity<RegistrationResult> {
        val registeredUser: RegistrationResult = authService.register(registration)
        return ResponseEntity.ok(registeredUser)
    }


    @Operation(summary = "Login in existing user account. Authentication not required.")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "User was successfully created",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = Token::class, description = "Access token for accessing secured endpoints")
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid input parameters",
            content = [Content(
                mediaType = APPLICATION_JSON_VALUE,
                schema = Schema(implementation = ErrorDetails::class)
            )]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Invalid username or password",
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
        description = "User login details",
        content = [Content(schema = Schema(implementation = Login::class))]
    )
    @PostMapping("/login", consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun authenticate(@RequestBody @Valid login: Login): ResponseEntity<Token> {
        val authenticated: Token = authService.login(login)
        return ResponseEntity.ok(authenticated)
    }
}
package net.azeti.challenge.recipe.exception


import io.jsonwebtoken.ExpiredJwtException
import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.TypeMismatchException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class ExceptionHandlerAdvice {

    @ExceptionHandler(NoSuchRecipeException::class)
    fun handleNotFound(e: NoSuchRecipeException): ResponseEntity<ErrorDetails> {
        logger.error("NoSuchRecipeException occurred: ${e.message}", e)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDetails(e.message))
    }

    @ExceptionHandler(InvalidInputException::class)
    fun handleInvalidInput(e: InvalidInputException): ResponseEntity<ErrorDetails> {
        logger.error("InvalidInputException occurred: ${e.message}", e)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDetails(e.message))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleInvalidInput(e: MethodArgumentNotValidException): ResponseEntity<ErrorDetails> {
        logger.error("MethodArgumentNotValidException occurred: ${e.message}", e)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDetails(e.message))
    }

    @ExceptionHandler(RecipeAlreadyExistsException::class)
    fun handleInvalidInput(e: RecipeAlreadyExistsException): ResponseEntity<ErrorDetails> {
        logger.error("RecipeAlreadyExistsException occurred: ${e.message}", e)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDetails(e.message))
    }
    @ExceptionHandler(BadCredentialsException::class)
    fun handleDeniedAccess(e: BadCredentialsException): ResponseEntity<ErrorDetails> {
        logger.error("BadCredentialsException occurred: ${e.message}", e)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDetails(e.message))
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun handleExpiredJwtException(e: ExpiredJwtException): ResponseEntity<ErrorDetails> {
        logger.error("BadCredentialsException occurred: ${e.message}", e)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDetails(e.message))
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleDeniedAccess(e: AccessDeniedException): ResponseEntity<ErrorDetails> {
        logger.error("AccessDeniedException occurred: ${e.message}", e)
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorDetails(e.message))
    }


    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException::class)
    fun handleConstraintViolation(e: ConstraintViolationException): ResponseEntity<ErrorDetails> {
        logger.error("ConstraintViolationException occurred: ${e.message}", e)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDetails(e.message))
    }

    @ExceptionHandler(TypeMismatchException::class)
    fun handleTypeMismatch(e: TypeMismatchException): ResponseEntity<ErrorDetails> {
        logger.error("TypeMismatchException occurred: ${e.message}", e)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDetails(e.message))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorDetails> {
        logger.error("Exception occurred: ${e.message}", e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails(e.message))
    }

    companion object {
        @JvmStatic
        var logger: Logger = LoggerFactory.getLogger(ExceptionHandlerAdvice::class.java)
    }
}
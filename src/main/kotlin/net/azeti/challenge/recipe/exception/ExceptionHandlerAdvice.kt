package net.azeti.challenge.recipe.exception


import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class ExceptionHandlerAdvice {

    @ExceptionHandler(NoSuchRecipeException::class)
    fun handleNotFound(e: NoSuchRecipeException): ResponseEntity<ErrorDetails> {
        // log exception
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorDetails(e.message))
    }

    @ExceptionHandler(InvalidInputException::class)
    fun handleInvalidInput(e: InvalidInputException): ResponseEntity<ErrorDetails> {
        // log exception
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDetails(e.message))
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleDeniedAccess(e: InvalidInputException): ResponseEntity<ErrorDetails> {
        // log exception
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorDetails(e.message))
    }


    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorDetails> {
        logger.error("Exception occurred", e)
        // log exception
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails(e.message))
    }

    companion object {
        @JvmStatic var logger: Logger = LoggerFactory.getLogger(ExceptionHandlerAdvice::class.java)
    }
}
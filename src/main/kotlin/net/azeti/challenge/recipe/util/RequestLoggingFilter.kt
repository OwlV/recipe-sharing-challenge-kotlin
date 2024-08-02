package net.azeti.challenge.recipe.util

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class RequestLoggingFilter : Filter {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest
        logRequestDetails(httpServletRequest)
        chain.doFilter(request, response)
    }

    private fun logRequestDetails(request: HttpServletRequest) {
        logger.info(
            "Incoming request: method={}, uri={}, params={}",
            request.method,
            request.requestURI,
            request.parameterMap
        )
    }

    companion object {
        @JvmStatic
        val logger: Logger = LoggerFactory.getLogger(RequestLoggingFilter::class.java)
    }
}
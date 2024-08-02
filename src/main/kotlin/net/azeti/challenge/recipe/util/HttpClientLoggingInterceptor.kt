package net.azeti.challenge.recipe.util

import org.slf4j.LoggerFactory
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

class HttpClientLoggingInterceptor : ClientHttpRequestInterceptor {
    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        val response = execution.execute(request, body)
        logResponse(response)
        return response
    }

    private fun logResponse(response: ClientHttpResponse) {
        val statusCode = response.statusCode
        val statusText = response.statusText
        val headers = response.headers

        val inputStream = response.body
        val bodyText = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))
            .lines()
            .collect(Collectors.joining("\n"))

        logger.info("Response Status: $statusCode $statusText")
        logger.info("Response Headers: $headers")
        logger.info("Response Body: $bodyText")
    }

    companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(HttpClientLoggingInterceptor::class.java)
    }

}
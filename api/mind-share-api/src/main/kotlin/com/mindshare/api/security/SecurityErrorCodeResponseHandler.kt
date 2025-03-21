package com.mindshare.api.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.mindshare.api.core.error.ErrorCode
import com.mindshare.api.core.web.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import java.nio.charset.StandardCharsets

open class SecurityErrorCodeResponseHandler(
    private val objectMapper: ObjectMapper
) {

    fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        errorCode: ErrorCode,
        errorMessage: String,
        status: Int
    ) {
        val errorResponse = ErrorResponse(errorMessage, errorCode)

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = status
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}
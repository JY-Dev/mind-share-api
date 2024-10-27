package com.mindshare.api.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.mindshare.api.core.error.ErrorCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class SecurityErrorCodeAuthenticationEntryPoint(
    objectMapper: ObjectMapper
) : SecurityErrorCodeResponseHandler(objectMapper), AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val status = HttpServletResponse.SC_UNAUTHORIZED
        val errorCode = ErrorCode.DEFAULT

        handle(request, response, errorCode, authException.message ?: "Unauthorized", status)
    }
}
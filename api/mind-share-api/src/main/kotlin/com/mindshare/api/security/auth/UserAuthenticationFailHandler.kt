package com.mindshare.api.security.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.mindshare.api.core.error.ErrorCode
import com.mindshare.api.core.error.ErrorResponse
import com.mindshare.api.security.auth.token.AuthTokenUseCase
import com.mindshare.api.security.presentation.model.response.LoginResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

class UserAuthenticationFailHandler(
    private val objectMapper: ObjectMapper
) : AuthenticationFailureHandler {


    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        val errorResponse = ErrorResponse(exception.message ?: "Unknown Exception", ErrorCode.DEFAULT)
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}
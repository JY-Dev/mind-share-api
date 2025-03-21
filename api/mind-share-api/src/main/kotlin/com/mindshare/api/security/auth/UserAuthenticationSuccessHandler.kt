package com.mindshare.api.security.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.mindshare.api.security.auth.token.AuthTokenUseCase
import com.mindshare.api.security.presentation.model.response.LoginResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

class UserAuthenticationSuccessHandler(
    private val authTokenUseCase: AuthTokenUseCase,
    private val objectMapper: ObjectMapper
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        try {
            if (authentication !is UserAuthentication) {
                throw AuthenticationServiceException("Authentication is not of type UserAuthentication")
            }

            val authToken = authTokenUseCase.issueToken(
                userId = authentication.userId,
                userType = authentication.userType
            )

            val loginResponse = LoginResponse(
                accessToken = authToken.accessToken,
                refreshToken = authToken.refreshToken
            )

            response.contentType = "application/json"
            response.characterEncoding = "UTF-8"
            response.status = HttpServletResponse.SC_OK
            response.writer.write(objectMapper.writeValueAsString(loginResponse))
            response.writer.flush()
        } catch (e: Exception) {
            response.contentType = "application/json"
            response.characterEncoding = "UTF-8"
            response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            response.writer.write("서버 내부 오류")
            response.writer.flush()
        }
    }
}
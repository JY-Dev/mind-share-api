package com.mindshare.api.security.presentation

import com.mindshare.api.core.error.ErrorCode
import com.mindshare.api.core.web.ErrorResponse
import com.mindshare.api.security.auth.token.AuthTokenUseCase
import com.mindshare.api.security.auth.token.error.RefreshTokenExpiredException
import com.mindshare.api.security.auth.token.error.TokenSecurityWarnException
import com.mindshare.api.security.presentation.model.request.LoginEmailRequest
import com.mindshare.api.security.presentation.model.request.ReissueTokenRequest
import com.mindshare.api.security.presentation.model.response.LoginResponse
import com.mindshare.api.security.presentation.model.response.ReissueTokenResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthApiController(
    private val authTokenUseCase: AuthTokenUseCase
) : AuthApi {
    override fun loginEmail(request: LoginEmailRequest): LoginResponse {
        throw NotImplementedError("Spring Security process login logic")
    }

    override fun logout(userId: Long) {
        authTokenUseCase.expireToken(userId)
    }

    override fun reissueToken(request: ReissueTokenRequest): ReissueTokenResponse {
        val authToken = authTokenUseCase.reissueToken(request.accessToken, request.refreshToken)
        return ReissueTokenResponse(authToken.accessToken, authToken.refreshToken)
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenSecurityWarnException::class, RefreshTokenExpiredException::class)
    fun handleError(exception: Exception): ErrorResponse {
        return ErrorResponse(exception.message ?: "unauthorized", ErrorCode.DEFAULT)
    }
}
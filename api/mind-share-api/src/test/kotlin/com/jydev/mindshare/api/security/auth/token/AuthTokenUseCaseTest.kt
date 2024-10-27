package com.jydev.mindshare.api.security.auth.token

import com.mindshare.api.security.auth.token.AuthTokenUseCase
import com.mindshare.api.security.auth.token.HandleSecurityWarnTokenUseCase
import com.mindshare.api.security.auth.token.JwtHelper
import com.mindshare.api.security.auth.token.error.RefreshTokenExpiredException
import com.mindshare.api.security.auth.token.error.TokenSecurityWarnException
import com.mindshare.api.security.config.AuthProperties
import com.mindshare.domain.auth.AuthRefreshToken
import com.mindshare.domain.auth.AuthRefreshTokenRepository
import com.mindshare.domain.user.UserType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.time.Instant

class AuthTokenUseCaseTest {

    private val jwtHelper = mock(JwtHelper::class.java)
    private val authProperties = mock(AuthProperties::class.java)
    private val refreshTokenRepository = mock(AuthRefreshTokenRepository::class.java)
    private val handleSecurityWarnTokenUseCase = mock(HandleSecurityWarnTokenUseCase::class.java)

    private val authTokenUseCase =
        AuthTokenUseCase(jwtHelper, authProperties, refreshTokenRepository, handleSecurityWarnTokenUseCase)


    @Test
    fun `리프레시 토큰이 만료된 경우 예외를 발생시킨다`() {
        val accessToken = "expired-access-token"
        val refreshToken = "expired-refresh-token"

        `when`(jwtHelper.getPayload(accessToken)).thenReturn(JwtHelper.TokenPayload(1L, UserType.USER, "session-id"))
        `when`(refreshTokenRepository.findByToken(refreshToken)).thenReturn(null)

        assertThrows<RefreshTokenExpiredException> {
            authTokenUseCase.reissueToken(accessToken, refreshToken)
        }
    }

    @Test
    fun `보안 경고 발생 시 관련 토큰에 대한 처리 로직 호출해야함`() {
        val accessToken = "access-token"
        val refreshToken = "refresh-token"

        val tokenPayload =
            JwtHelper.TokenPayload(userId = 1L, userType = UserType.USER, sessionId = "invalid-session-id")
        val authRefreshToken = AuthRefreshToken(
            userId = 2L,
            sessionId = "valid-session-id",
            expirationTime = Instant.now().plusSeconds(3600)
        )

        `when`(jwtHelper.getPayload(accessToken)).thenReturn(tokenPayload)
        `when`(refreshTokenRepository.findByToken(refreshToken)).thenReturn(authRefreshToken)

        assertThrows<TokenSecurityWarnException> {
            authTokenUseCase.reissueToken(accessToken, refreshToken)
        }

        verify(handleSecurityWarnTokenUseCase).invoke(1L) // AccessToken User ID
        verify(handleSecurityWarnTokenUseCase).invoke(2L) // RefreshToken User ID
    }

    @Test
    fun `리프레시 토큰이 유효한 경우 새로운 토큰을 발급한다`() {
        val accessToken = "access-token"
        val refreshToken = "refresh-token"
        val tokenPayload = JwtHelper.TokenPayload(userId = 1L, userType = UserType.USER, sessionId = "session-id")
        val authRefreshToken =
            AuthRefreshToken(userId = 1L, sessionId = "session-id", expirationTime = Instant.now().plusSeconds(3600))

        `when`(jwtHelper.getPayload(accessToken)).thenReturn(tokenPayload)
        `when`(refreshTokenRepository.findByToken(refreshToken)).thenReturn(authRefreshToken)
        `when`(jwtHelper.reIssueToken(accessToken)).thenReturn("new-access-token")

        val result = authTokenUseCase.reissueToken(accessToken, refreshToken)

        assertEquals("new-access-token", result.accessToken)
        assertNotEquals(refreshToken, result.refreshToken)
    }
}
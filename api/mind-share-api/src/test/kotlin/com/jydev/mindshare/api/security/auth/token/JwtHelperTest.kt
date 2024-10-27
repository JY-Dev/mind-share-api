package com.jydev.mindshare.api.security.auth.token

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.mindshare.api.security.config.AuthProperties
import com.mindshare.api.security.auth.token.JwtHelper
import com.mindshare.api.security.auth.token.JwtHelper.Companion.resolveBearerToken
import com.mindshare.api.security.auth.token.JwtHelper.TokenPayload
import com.mindshare.domain.user.UserType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.Duration

class JwtHelperTest {

    private lateinit var jwtHelper: JwtHelper
    private lateinit var shortTtlJwtHelper: JwtHelper
    private val objectMapper = ObjectMapper().registerKotlinModule()

    @BeforeEach
    fun setup() {
        val authProperties = mock(AuthProperties::class.java).apply {
            `when`(secretKey).thenReturn("my-secret-key-which-is-very-secret-and-long-enough")
            `when`(accessTokenTtl).thenReturn(Duration.ofMinutes(5))
        }
        val shortTtlProperties = mock(AuthProperties::class.java).apply {
            `when`(secretKey).thenReturn("my-secret-key-which-is-very-secret-and-long-enough")
            `when`(accessTokenTtl).thenReturn(Duration.ofMillis(10))
        }
        jwtHelper = JwtHelper(authProperties, objectMapper)
        shortTtlJwtHelper = JwtHelper(shortTtlProperties, objectMapper)
    }

    @Test
    fun `토큰 발급이 정상적으로 이루어지는지`() {
        // given
        val payload = TokenPayload(
            userId = 1L,
            userType = UserType.USER,
            sessionId = "session-123"
        )

        // when
        val token = jwtHelper.issueToken(payload)

        // then
        assertNotNull(token, "토큰이 정상적으로 발급되었는지 확인합니다.")
    }

    @Test
    fun `발급된 토큰이 유효한지 확인`() {
        // given
        val payload = TokenPayload(
            userId = 1L,
            userType = UserType.USER,
            sessionId = "session-123"
        )
        val token = jwtHelper.issueToken(payload)

        // when
        val isValid = jwtHelper.isValidToken(token)

        // then
        assertTrue(isValid, "발급된 토큰이 유효한지 확인합니다.")
    }

    @Test
    fun `만료된 토큰이 유효하지 않은지 확인`() {
        // given
        val payload = TokenPayload(
            userId = 1L,
            userType = UserType.USER,
            sessionId = "session-123"
        )
        val expiredToken = shortTtlJwtHelper.issueToken(payload)

        Thread.sleep(500)

        // when
        val isValid = shortTtlJwtHelper.isValidToken(expiredToken)

        // then
        assertFalse(isValid, "만료된 토큰이 유효하지 않음을 확인합니다.")
    }

    @Test
    fun `토큰 재발급이 정상적으로 이루어지는지 확인`() {
        // given
        val payload = TokenPayload(
            userId = 1L,
            userType = UserType.USER,
            sessionId = "session-123"
        )
        val oldToken = jwtHelper.issueToken(payload)

        // when
        val newToken = jwtHelper.reIssueToken(oldToken)

        val oldPayload = jwtHelper.getPayload(oldToken)
        val newPayload = jwtHelper.getPayload(newToken)
        // then
        assertNotNull(newToken, "재발급된 토큰이 정상적으로 존재하는지 확인합니다.")
        assertEquals(oldPayload, newPayload, "재발급된 토큰의 페이로드가 이전 토큰과 깉은지 확인합니다.")
    }

    @Test
    fun `잘못된 토큰으로 재발급 시 예외가 발생하는지 확인`() {
        // given
        val invalidToken = "invalid-token"

        // when & then
        assertThrows(Exception::class.java) {
            jwtHelper.reIssueToken(invalidToken)
        }
    }

    @Test
    fun `올바른 Bearer 토큰 문자열에서 토큰을 추출할 수 있는지 확인`() {
        // given
        val token = "some-jwt-token"
        val bearerToken = "Bearer $token"

        // when
        val resolvedToken = bearerToken.resolveBearerToken()

        // then
        assertEquals(token, resolvedToken, "Bearer 문자열에서 올바르게 토큰을 추출했는지 확인합니다.")
    }
}
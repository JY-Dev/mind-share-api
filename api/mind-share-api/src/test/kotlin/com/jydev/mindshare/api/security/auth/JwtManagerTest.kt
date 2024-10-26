package com.jydev.mindshare.api.security.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.mindshare.api.security.config.AuthProperties
import com.mindshare.api.security.auth.JwtManager
import com.mindshare.api.security.auth.JwtManager.Companion.resolveBearerToken
import com.mindshare.api.security.auth.JwtManager.TokenPayload
import com.mindshare.domain.user.UserType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.Duration

class JwtManagerTest {

    private lateinit var jwtManager: JwtManager
    private lateinit var shortTtlJwtManager: JwtManager
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
        jwtManager = JwtManager(authProperties, objectMapper)
        shortTtlJwtManager = JwtManager(shortTtlProperties, objectMapper)
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
        val token = jwtManager.issueToken(payload)

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
        val token = jwtManager.issueToken(payload)

        // when
        val isValid = jwtManager.isValidToken(token)

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
        val expiredToken = shortTtlJwtManager.issueToken(payload)

        Thread.sleep(500)

        // when
        val isValid = shortTtlJwtManager.isValidToken(expiredToken)

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
        val oldToken = jwtManager.issueToken(payload)

        // when
        val newToken = jwtManager.reIssueToken(oldToken)

        val oldPayload = jwtManager.getPayload(oldToken)
        val newPayload = jwtManager.getPayload(newToken)
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
            jwtManager.reIssueToken(invalidToken)
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
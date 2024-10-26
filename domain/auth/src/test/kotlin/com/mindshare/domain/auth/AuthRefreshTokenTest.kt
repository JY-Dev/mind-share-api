package com.mindshare.domain.auth

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant
import java.util.*

class AuthRefreshTokenTest {

    private lateinit var authRefreshToken: AuthRefreshToken
    private val userId = 1L
    private val sessionId = UUID.randomUUID().toString()
    private val initialExpirationTime = Instant.now().plus(Duration.ofHours(1)) // 1시간 후 만료

    @BeforeEach
    fun setup() {
        authRefreshToken = AuthRefreshToken(
            userId = userId,
            sessionId = sessionId,
            expirationTime = initialExpirationTime
        )
    }

    @Test
    fun `isExpired 메서드가 만료되지 않은 토큰에 대해 false를 반환하는지 확인`() {
        // given
        val now = Instant.now()

        // when
        val result = authRefreshToken.isExpired(now)

        // then
        assertFalse(result, "만료되지 않은 토큰에 대해 isExpired 메서드는 false를 반환해야 합니다.")
    }

    @Test
    fun `isExpired 메서드가 만료된 토큰에 대해 true를 반환하는지 확인`() {
        // given
        val now = initialExpirationTime.plus(Duration.ofMinutes(1)) // 만료 시간보다 이후 시간

        // when
        val result = authRefreshToken.isExpired(now)

        // then
        assertTrue(result, "만료된 토큰에 대해 isExpired 메서드는 true를 반환해야 합니다.")
    }

    @Test
    fun `rotationToken 메서드가 새로운 토큰과 연장된 만료 시간을 설정하는지 확인`() {
        // given

        // when
        val oldToken = authRefreshToken.token
        authRefreshToken.rotationToken(Duration.ofMillis(10L))

        Thread.sleep(20L)
        assertTrue(authRefreshToken.isExpired(Instant.now()), "Refresh Token 만료")

        authRefreshToken.rotationToken(Duration.ofMillis(30L))
        assertFalse(authRefreshToken.isExpired(Instant.now()), "토큰 기간 연장으로 Refresh Token가 만료되지않음")

        val newToken = authRefreshToken.token

        assertNotEquals(oldToken, newToken, "rotationToken 메서드는 새로운 토큰 값을 생성해야 합니다.")
    }

    @Test
    fun `isSameSession 메서드가 동일한 세션 ID에 대해 true를 반환하는지 확인`() {
        // when
        val result = authRefreshToken.isSameSession(sessionId)

        // then
        assertTrue(result, "isSameSession 메서드는 동일한 세션 ID에 대해 true를 반환해야 합니다.")
    }

    @Test
    fun `isSameSession 메서드가 다른 세션 ID에 대해 false를 반환하는지 확인`() {
        // given
        val anotherSessionId = UUID.randomUUID().toString()

        // when
        val result = authRefreshToken.isSameSession(anotherSessionId)

        // then
        assertFalse(result, "isSameSession 메서드는 다른 세션 ID에 대해 false를 반환해야 합니다.")
    }

    @Test
    fun `equals 메서드가 동일한 ID를 가진 객체를 같은 객체로 인식하는지 확인`() {
        // given
        val anotherToken = AuthRefreshToken(
            userId = userId,
            sessionId = sessionId,
            expirationTime = initialExpirationTime
        )
        anotherToken.id = authRefreshToken.id // 동일한 ID 설정

        // then
        assertEquals(authRefreshToken, anotherToken, "equals 메서드는 동일한 ID를 가진 객체를 같은 객체로 인식해야 합니다.")
    }

    @Test
    fun `equals 메서드가 ID가 다른 객체를 다른 객체로 인식하는지 확인`() {
        // given
        val anotherToken = AuthRefreshToken(
            userId = userId,
            sessionId = sessionId,
            expirationTime = initialExpirationTime,
            id = 2L
        )

        // then
        assertNotEquals(authRefreshToken, anotherToken, "equals 메서드는 ID가 다른 객체를 다른 객체로 인식해야 합니다.")
    }

    @Test
    fun `hashCode 메서드가 동일한 ID를 가진 객체에서 동일한 해시코드를 반환하는지 확인`() {
        // given
        val anotherToken = AuthRefreshToken(
            userId = userId,
            sessionId = sessionId,
            expirationTime = initialExpirationTime
        )
        anotherToken.id = authRefreshToken.id // 동일한 ID 설정

        // then
        assertEquals(authRefreshToken.hashCode(), anotherToken.hashCode(), "hashCode 메서드는 동일한 ID를 가진 객체에서 동일한 해시코드를 반환해야 합니다.")
    }
}
package com.jydev.mindshare.api.core.util

import com.mindshare.api.core.util.toPageToken
import com.mindshare.api.core.util.toPairPageToken
import com.mindshare.api.core.util.toTypeValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.Instant

class PageTokenUtilTest {

    @Test
    fun `문자열과 정수 페어를 정상적으로 토큰으로 인코딩하고 디코딩할 수 있다`() {
        // given
        val pair = "Kotlin" to 42

        // when
        val token = pair.toPageToken()

        // then
        val decodedPair = token.toPairPageToken<String, Int>()
        assertEquals(pair, decodedPair)
    }

    @Test
    fun `문자열과 Instant 페어를 정상적으로 토큰으로 인코딩하고 디코딩할 수 있다`() {
        // given
        val now = Instant.now()
        val pair = "Timestamp" to now

        // when
        val token = pair.toPageToken()

        // then
        val decodedPair = token.toPairPageToken<String, Instant>()
        assertEquals(pair.first, decodedPair.first)
        assertEquals(pair.second.toEpochMilli(), decodedPair.second.toEpochMilli())
    }

    @Test
    fun `정수와 부울 페어를 정상적으로 토큰으로 인코딩하고 디코딩할 수 있다`() {
        // given
        val pair = 123L to true

        // when
        val token = pair.toPageToken()

        // then
        val decodedPair = token.toPairPageToken<Long, Boolean>()
        assertEquals(pair, decodedPair)
    }

    @Test
    fun `잘못된 토큰 형식이 주어졌을 때 IllegalArgumentException이 발생한다`() {
        // given
        val invalidToken = "invalidToken"

        // when & then
        assertThrows(IllegalArgumentException::class.java) {
            invalidToken.toPairPageToken<String, Int>()
        }
    }

    @Test
    fun `지원되지 않는 타입을 사용할 경우 IllegalArgumentException이 발생한다`() {
        // given
        val pair = "Kotlin" to 123.45
        val token = pair.toPageToken()

        // when & then
        assertThrows(IllegalArgumentException::class.java) {
            token.toPairPageToken<String, Double>()
        }
    }

    @Test
    fun `문자열을 정수 타입으로 변환할 수 있다`() {
        // given
        val intString = "123"

        // when
        val intValue = intString.toTypeValue<Int>()

        // then
        assertEquals(123, intValue)
    }

    @Test
    fun `문자열을 Instant 타입으로 변환할 수 있다`() {
        // given
        val now = Instant.now().toEpochMilli().toString()

        // when
        val instantValue = now.toTypeValue<Instant>()

        // then
        assertEquals(Instant.ofEpochMilli(now.toLong()), instantValue)
    }

}
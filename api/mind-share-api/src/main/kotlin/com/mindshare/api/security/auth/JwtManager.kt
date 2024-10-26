package com.mindshare.api.security.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.mindshare.api.security.config.AuthProperties
import com.mindshare.domain.user.UserType
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtManager(
    private val authProperties: AuthProperties,
    private val objectMapper: ObjectMapper
) {

    private val signingKey: SecretKey by lazy {
        val keyBytes = authProperties.secretKey.toByteArray()
        Keys.hmacShaKeyFor(keyBytes)
    }

    fun reIssueToken(oldToken: String): String {
        val payload = getPayload(oldToken)
        return issueToken(payload)
    }

    fun getPayload(token : String) : TokenPayload =
        getClaims(token)[PAYLOAD_KEY, String::class.java]?.let {
            objectMapper.readValue(it, TokenPayload::class.java)
        } ?: throw IllegalArgumentException("Payload does not exist")

    fun issueToken(payload: TokenPayload): String {
        val payloadJson = objectMapper.writeValueAsString(payload)
        val claims = mutableMapOf<String, Any>(
            PAYLOAD_KEY to payloadJson
        )
        return createToken(claims)
    }

    private fun createToken(claims: Map<String, Any>): String {
        val issuedAt = Date.from(Instant.now())

        val accessTokenTtl = authProperties.accessTokenTtl
        val expirationInstant = Instant.now().plus(accessTokenTtl.toSeconds(), ChronoUnit.SECONDS)
        val expirationAt = Date.from(expirationInstant)

        return Jwts.builder()
            .issuer(ISS)
            .claims(claims)
            .issuedAt(issuedAt)
            .expiration(expirationAt)
            .signWith(signingKey)
            .compact()
    }


    fun isValidToken(accessToken: String?): Boolean {
        if (accessToken.isNullOrBlank()) return false
        return try {
            !isTokenExpired(accessToken)
        } catch (e: Exception) {
            false
        }
    }

    fun isTokenExpired(token: String): Boolean {
        val expiration = getClaims(token).expiration
        return expiration.before(Date.from(Instant.now()))
    }

    private fun getClaims(token: String): Claims {
        return try {
            Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }

    companion object {
        private const val PAYLOAD_KEY = "payload"
        private const val ISS = "mind-share"

        fun String.resolveBearerToken(): String? {
            return this.takeIf { it.startsWith("Bearer ") }?.substring(7)
        }

    }

    data class TokenPayload(
        val userId: Long,
        val userType: UserType,
        val sessionId: String
    )
}
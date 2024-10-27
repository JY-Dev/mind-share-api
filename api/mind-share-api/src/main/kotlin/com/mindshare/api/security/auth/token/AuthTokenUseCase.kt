package com.mindshare.api.security.auth.token

import com.mindshare.api.security.config.AuthProperties
import com.mindshare.domain.auth.AuthRefreshToken
import com.mindshare.domain.auth.AuthRefreshTokenRepository
import com.mindshare.domain.user.UserType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class AuthTokenUseCase(
    private val jwtHelper: JwtHelper,
    private val authProperties: AuthProperties,
    private val refreshTokenRepository: AuthRefreshTokenRepository
) {

    @Transactional
    fun issueToken(userId : Long, userType: UserType) : AuthToken {

        // RefreshToken이 이미 존재한다면 제거
        refreshTokenRepository.findByUserId(userId)?.let(refreshTokenRepository::delete)

        // Refresh Token 생성
        val refreshTokenExpirationTime = Instant.now().plus(authProperties.refreshTokenTtl)
        val refreshToken = AuthRefreshToken(
            userId = userId,
            expirationTime = refreshTokenExpirationTime
        )
        val savedRefreshToken = refreshTokenRepository.save(refreshToken)

        // AccessToken 생성
        val tokenPayload = JwtHelper.TokenPayload(
            userId = userId,
            userType = userType,
            sessionId = savedRefreshToken.sessionId
        )
        val accessToken = jwtHelper.issueToken(tokenPayload)

        return AuthToken(accessToken, savedRefreshToken.token)
    }
}
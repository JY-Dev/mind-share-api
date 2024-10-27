package com.mindshare.api.security.auth.token

import com.mindshare.api.security.auth.token.error.RefreshTokenExpiredException
import com.mindshare.api.security.auth.token.error.TokenSecurityWarnException
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
    private val refreshTokenRepository: AuthRefreshTokenRepository,
    private val handleSecurityWarnTokenUseCase: HandleSecurityWarnTokenUseCase
) {

    @Transactional
    fun issueToken(userId: Long, userType: UserType): AuthToken {

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

    @Transactional
    fun reissueToken(accessToken: String, refreshToken: String): AuthToken {
        val tokenPayload = jwtHelper.getPayload(accessToken)
        val authRefreshToken = refreshTokenRepository.findByToken(refreshToken)
            ?: throw RefreshTokenExpiredException("RefreshToken is expired")

        val isSameSessionToken = authRefreshToken.isSameSession(tokenPayload.sessionId)
        val isSameUser = tokenPayload.userId == authRefreshToken.userId

        // 토큰 보안 이슈가 생긴 경우
        if (isSameSessionToken.not() || isSameUser.not()) {
            // AccessToken 유저
            handleSecurityWarnTokenUseCase(tokenPayload.userId)
            // RefreshToken 유저
            handleSecurityWarnTokenUseCase(authRefreshToken.userId)
            throw TokenSecurityWarnException("Token has security warn")
        }

        // Refresh Token 만료 확인
        if (authRefreshToken.isExpired(Instant.now())) {
            throw RefreshTokenExpiredException("Session is invalidated")
        }

        // 토큰 갱신
        authRefreshToken.rotationToken(authProperties.refreshTokenTtl)
        val newAccessToken = jwtHelper.reIssueToken(accessToken)
        val newRefreshToken = authRefreshToken.token

        return AuthToken(newAccessToken, newRefreshToken)
    }

    @Transactional
    fun expireToken(userId : Long) {
        refreshTokenRepository.findByUserId(userId)?.let(refreshTokenRepository::delete)
    }
}
package com.mindshare.domain.auth

interface AuthRefreshTokenRepository {
    fun findBySessionId() : AuthRefreshToken?
}
package com.mindshare.domain.auth

interface AuthRefreshTokenRepository {
    fun findBySessionId(sessionId : String) : AuthRefreshToken?
    fun findByUserId(userId : Long) : AuthRefreshToken?
    fun save(authRefreshToken: AuthRefreshToken) : AuthRefreshToken
    fun delete(authRefreshToken: AuthRefreshToken)
}
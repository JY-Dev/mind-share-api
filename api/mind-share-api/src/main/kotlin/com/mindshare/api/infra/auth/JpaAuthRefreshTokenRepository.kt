package com.mindshare.api.infra.auth

import com.mindshare.domain.auth.AuthRefreshToken
import com.mindshare.domain.auth.AuthRefreshTokenRepository
import org.springframework.data.jpa.repository.JpaRepository

interface JpaAuthRefreshTokenRepository : JpaRepository<AuthRefreshToken, Long>, AuthRefreshTokenRepository {
}
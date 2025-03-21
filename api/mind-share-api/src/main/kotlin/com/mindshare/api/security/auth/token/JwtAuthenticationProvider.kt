package com.mindshare.api.security.auth.token

import com.mindshare.api.security.auth.UserAuthentication
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

class JwtAuthenticationProvider(
    private val jwtHelper: JwtHelper
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication? {
        if (authentication !is AuthTokenAuthentication) {
            return null
        }

        val token = authentication.accessToken
        val validToken = jwtHelper.isValidToken(token)
        if (validToken.not()) {
            return null
        }

        val payload = jwtHelper.getPayload(token)

        return UserAuthentication(payload.userId, payload.userType, true)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication.let(AuthTokenAuthentication::class.java::isAssignableFrom)
    }
}
package com.mindshare.api.security.auth.token

import com.mindshare.api.security.auth.token.JwtHelper.Companion.resolveBearerToken
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationConverter

class AuthTokenAuthenticationConverter : AuthenticationConverter {

    override fun convert(request: HttpServletRequest): Authentication? {
        val bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return null

        val accessToken = bearerToken.resolveBearerToken() ?: return null

        return AuthTokenAuthentication(accessToken)
    }
}
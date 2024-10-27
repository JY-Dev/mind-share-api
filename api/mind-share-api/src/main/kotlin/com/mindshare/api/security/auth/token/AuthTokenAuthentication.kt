package com.mindshare.api.security.auth.token

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class AuthTokenAuthentication(
    val accessToken: String
) : Authentication {
    private var authenticated = false

    override fun getName(): String {
        return "AuthToken"
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getCredentials(): Any {
        return accessToken
    }

    override fun getDetails(): Any? {
        return null
    }

    override fun getPrincipal(): Any {
        return accessToken
    }

    override fun isAuthenticated(): Boolean {
        return authenticated
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.authenticated = isAuthenticated
    }
}
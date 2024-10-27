package com.mindshare.api.security.auth

import com.mindshare.domain.user.UserType
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class UserAuthentication(
    val userId: Long,
    val userType: UserType,
    private var isAuthenticated: Boolean = false
) : Authentication {

    override fun getName(): String {
        return userId.toString()
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val role = when (userType) {
            UserType.ADMIN -> "ROLE_ADMIN"
            UserType.USER -> "ROLE_USER"
        }
        return mutableListOf(SimpleGrantedAuthority(role))
    }

    override fun getCredentials(): Any? {
        return null
    }

    override fun getDetails(): Any? {
        return null
    }

    override fun getPrincipal(): Long {
        return userId
    }

    override fun isAuthenticated(): Boolean {
        return isAuthenticated
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.isAuthenticated = isAuthenticated
    }
}
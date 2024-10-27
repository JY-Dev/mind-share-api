package com.mindshare.api.security.auth.account.email

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class EmailAccountAuthentication(
    val email: String,
    val password: String
) : Authentication {

    override fun getName(): String {
        return email
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getCredentials(): String {
        return password
    }

    override fun getDetails(): Any? {
        return null
    }

    override fun getPrincipal(): String {
        return email
    }

    override fun isAuthenticated(): Boolean {
        return false
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        throw UnsupportedOperationException("인증 수행을 위한 객체이기 때문에 항상 unauthenticated 상태입니다.")
    }
}

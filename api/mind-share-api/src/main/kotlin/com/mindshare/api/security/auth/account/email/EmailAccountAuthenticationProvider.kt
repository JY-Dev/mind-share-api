package com.mindshare.api.security.auth.account.email

import com.mindshare.api.security.auth.UserAuthentication
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

class EmailAccountAuthenticationProvider(
    private val authenticateUserCase: EmailAccountAuthenticationUseCase
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication? {
        if (authentication !is EmailAccountAuthentication) {
            return null
        }

        val email = authentication.email
        val password = authentication.password
        val authenticateUser = authenticateUserCase(email, password)

        return UserAuthentication(
            userId = authenticateUser.id!!,
            userType = authenticateUser.userType,
            isAuthenticated = true
        )
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication.let(EmailAccountAuthentication::class.java::isAssignableFrom)
    }
}
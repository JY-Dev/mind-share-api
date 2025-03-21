package com.mindshare.api.security.auth.account

import com.fasterxml.jackson.databind.ObjectMapper
import com.mindshare.api.security.auth.account.email.EmailAccountAuthentication
import com.mindshare.api.security.presentation.model.request.LoginEmailRequest
import com.mindshare.domain.auth.AccountProvider
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationConverter

class AccountAuthenticationConverter(
    private val objectMapper: ObjectMapper,
) : AuthenticationConverter {


    override fun convert(request: HttpServletRequest): Authentication? {
        val provider = extractProviderFromURI(request.requestURI) ?: return null

        return when (provider) {
            AccountProvider.EMAIL -> {
                val loginEmailRequest = deserializeRequest(request, LoginEmailRequest::class.java)
                EmailAccountAuthentication(loginEmailRequest.email, loginEmailRequest.password)
            }
        }
    }

    private fun extractProviderFromURI(requestURI: String): AccountProvider? {
        val providerParam = requestURI.removePrefix("/auth/login/").uppercase()
        return AccountProvider.entries.find { it.name == providerParam }
    }

    private fun <T> deserializeRequest(request: HttpServletRequest, clazz: Class<T>): T {
        return objectMapper.readValue(request.inputStream, clazz)
    }
}
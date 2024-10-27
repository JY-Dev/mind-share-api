package com.mindshare.api.security.presentation

import com.mindshare.api.security.presentation.model.request.LoginEmailRequest
import com.mindshare.api.security.presentation.model.response.LoginResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthApiController : AuthApi {
    override fun loginEmail(request: LoginEmailRequest): LoginResponse {
        throw NotImplementedError("Spring Security process login logic")
    }
}
package com.mindshare.api.presentation.account

import com.mindshare.api.application.auth.RegisterEmailAccountUseCase
import com.mindshare.api.presentation.account.model.request.SignupEmailRequest
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountApiController(
    val registerEmailAccountUseCase: RegisterEmailAccountUseCase
) : AccountApi {

    override fun signupEmail(request: SignupEmailRequest) {

        registerEmailAccountUseCase(
            nickname = request.user.nickname,
            email = request.email,
            password = request.password,
        )
    }
}
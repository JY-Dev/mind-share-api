package com.mindshare.api.presentation.account

import com.mindshare.api.presentation.account.model.request.SignupEmailRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@RequestMapping("/account")
interface AccountApi {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup/email")
    fun signupEmail(@RequestBody @Valid request: SignupEmailRequest)
}
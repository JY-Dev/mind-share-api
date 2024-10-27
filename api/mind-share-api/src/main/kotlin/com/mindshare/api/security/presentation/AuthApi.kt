package com.mindshare.api.security.presentation

import com.mindshare.api.core.error.ErrorResponse
import com.mindshare.api.security.presentation.model.request.LoginEmailRequest
import com.mindshare.api.security.presentation.model.response.LoginResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Tag(
    name = "Auth API",
    description = """
        인증 관련 API 입니다."""
)
@RequestMapping("/auth")
interface AuthApi {

    @Operation(
        summary = "이메일 회원 가입",
        description = """
                    이메일 회원 가입을 합니다."""
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상"),
        ApiResponse(
            responseCode = "401", description = "로그인 실패",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        ),
        ApiResponse(responseCode = "500", description = "서버 오류")
    )
    @PostMapping("/login/email")
    fun loginEmail(@RequestBody request: LoginEmailRequest) : LoginResponse
}
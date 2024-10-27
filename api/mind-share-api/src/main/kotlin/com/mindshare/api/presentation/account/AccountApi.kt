package com.mindshare.api.presentation.account

import com.mindshare.api.core.error.ErrorResponse
import com.mindshare.api.presentation.account.model.request.SignupEmailRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@Tag(
    name = "Acccount API",
    description = """
        계정 관련 API 입니다."""
)
@RequestMapping("/account")
interface AccountApi {

    @Operation(
        summary = "이메일 회원 가입",
        description = """
                    이메일 회원 가입을 합니다."""
    )
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "정상"), ApiResponse(
            responseCode = "400", description = """
                            * 에러코드 
                            
                            - A01001 : 닉네임 중복
                            - A01002 : 이메일 중복
                            - A05001 : 요청한 데이터가 요구사항을 충족하지 않습니다.
                            """,
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup/email")
    fun signupEmail(@RequestBody @Valid request: SignupEmailRequest)
}
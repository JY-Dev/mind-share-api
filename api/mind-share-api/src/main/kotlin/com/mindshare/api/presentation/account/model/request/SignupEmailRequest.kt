package com.mindshare.api.presentation.account.model.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class SignupEmailRequest(

    @Schema(description = """
        유저정보
        """",
        requiredMode = Schema.RequiredMode.REQUIRED)
    val user: UserRequest,

    @Schema(description = """
        이메일
        """",
        requiredMode = Schema.RequiredMode.REQUIRED)
    @field:Email
    val email: String,

    @Schema(description = """
        비밀번호는 영문 소문자와 숫자로 이루어진 1~12자만 허용합니다.
        """",
        requiredMode = Schema.RequiredMode.REQUIRED)
    @field:Pattern(regexp = "^[a-z0-9]+$", message = "비밀번호는 영문 소문자와 숫자만 허용됩니다.")
    @field:Size(min = 6, max = 12, message = "비밀번호는 6자 이상 12자 이하여야 합니다.")
    val password: String
)

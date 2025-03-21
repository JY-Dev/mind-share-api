package com.mindshare.api.presentation.account.model.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UserRequest(
    @Schema(
        description = """
        닉네임은 영문 소문자와 숫자로 이루어진 1~12자만 허용합니다.
        """",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @field:Pattern(regexp = "^[a-z0-9]+$", message = "닉네임은 영문 소문자와 숫자만 허용됩니다.")
    @field:Size(min = 1, max = 12, message = "닉네임은 1자 이상 12자 이하여야 합니다.")
    val nickname: String
)

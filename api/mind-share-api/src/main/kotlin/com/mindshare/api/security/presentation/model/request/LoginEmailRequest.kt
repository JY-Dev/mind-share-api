package com.mindshare.api.security.presentation.model.request

import io.swagger.v3.oas.annotations.media.Schema

data class LoginEmailRequest(

    @Schema(
        description = """
        로그인 이메일
        """",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val email: String,

    @Schema(
        description = """
        비밀번호
        """",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val password: String
)

package com.mindshare.api.security.presentation.model.response

import io.swagger.v3.oas.annotations.media.Schema

data class ReissueTokenResponse(

    @Schema(description = """
        엑세스 토큰
        """",
        requiredMode = Schema.RequiredMode.REQUIRED)
    val accessToken: String,

    @Schema(description = """
        리프레시 토큰
        """",
        requiredMode = Schema.RequiredMode.REQUIRED)
    val refreshToken: String
)

package com.mindshare.api.security.presentation.model.request

import io.swagger.v3.oas.annotations.media.Schema

data class ReissueTokenRequest(

    @Schema(
        description = """
        엑세스 토큰
        """",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val accessToken: String,

    @Schema(
        description = """
        리프레시 토큰
        """",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val refreshToken: String
)

package com.mindshare.api.core.web

import io.swagger.v3.oas.annotations.media.Schema

data class PagingResponse(

    @Schema(
        description = """
        PageToken (페이징 하기 위한 Key 역할) 
        null 인 경우는 nextPage가 없는 경우
        """",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        nullable = true
    )
    val pageToken: String?,

    @Schema(
        description = """
        다음 페이지가 존재하는 경우 true
        다음 페이지가 존재하지 않는 경우 false
        """",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val hasNext: Boolean,
)

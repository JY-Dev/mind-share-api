package com.mindshare.api.presentation.post.model.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size

data class EditPostRequest(

    @Schema(description = """
        글 제목
        """",
        requiredMode = Schema.RequiredMode.REQUIRED)
    @field:Size(min = 1, max = 30, message = "제목은 1자 이상 30자 이하여야 합니다.")
    val title : String,

    @Schema(description = """
        글 내용
        """",
        requiredMode = Schema.RequiredMode.REQUIRED)
    @field:Size(min = 1, max = 250, message = "내용은 1자 이상 250자 이하여야 합니다.")
    val content : String,
)

package com.mindshare.api.presentation.post.model.response

import io.swagger.v3.oas.annotations.media.Schema

data class CreatePostResponse(

    @Schema(
        description = """
        글 아이디
        """",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val postId: Long
)

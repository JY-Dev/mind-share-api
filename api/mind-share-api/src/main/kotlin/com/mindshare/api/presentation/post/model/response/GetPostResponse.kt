package com.mindshare.api.presentation.post.model.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class GetPostResponse(

    @Schema(
        description = """
        게시글 제목
        """",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val title: String,

    @Schema(
        description = """
        게시글 내용
        """",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val content: String,

    @Schema(
        description = """
        게시글 조회수
        """",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val viewCount: Long,

    @Schema(
        description = """
        게시글 생성자 닉네임
        닉네임이 null인 경우 삭제된 유저
        """",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        nullable = true
    )
    val nickname: String?,

    @Schema(
        description = """
        게시글 생성 시간
        e.g. 2023-10-09T22:55:55
        """",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "2023-10-09T22:55:55"
    )
    val creationTime: LocalDateTime
) {
}

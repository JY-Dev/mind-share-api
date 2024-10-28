package com.mindshare.api.presentation.post.model.response

import com.mindshare.api.core.web.PagingResponse
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class ListPostResponse(

    @Schema(description = """
        게시글 목록 데이터
        """",
        requiredMode = Schema.RequiredMode.REQUIRED)
    val contents: List<ListPostItemResponse>,

    @Schema(
        description = """
        Page 관련 데이터
        """",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val paging: PagingResponse
) {

    data class ListPostItemResponse(

        @Schema(
            description = """
        게시글 Id
        """",
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        val postId: Long,

        @Schema(
            description = """
        게시글 제목
        """",
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        val title: String,

        @Schema(
            description = """
        게시글 생성자 닉네임
        """",
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        val nickname: String,

        @Schema(
            description = """
        게시글 생성 시간
        e.g. 2023-10-09T22:55:55
        """",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "2023-10-09T22:55:55"
        )
        val creationTime: LocalDateTime
    )
}

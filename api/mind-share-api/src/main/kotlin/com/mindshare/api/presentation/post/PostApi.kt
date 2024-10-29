package com.mindshare.api.presentation.post

import com.mindshare.api.core.error.ErrorResponse
import com.mindshare.api.presentation.post.model.request.CreatePostRequest
import com.mindshare.api.presentation.post.model.request.EditPostRequest
import com.mindshare.api.presentation.post.model.response.CreatePostResponse
import com.mindshare.api.presentation.post.model.response.GetPostResponse
import com.mindshare.api.presentation.post.model.response.ListPostResponse
import com.mindshare.domain.post.Post
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam


@Tag(
    name = "Post API",
    description = """
        게시글 관련 API 입니다."""
)
@RequestMapping("/posts")
interface PostApi {

    @Operation(
        summary = "게시글 작성",
        description = """
                    게시글을 작성 합니다."""
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상"), ApiResponse(
            responseCode = "400", description = """
                            * 에러코드 
                            
                            - A05001 : 요청한 데이터가 요구사항을 충족하지 않습니다.
                            """,
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )
    )
    @SecurityRequirement(name = "AccessToken")
    @PostMapping
    fun createPost(
        @RequestBody @Valid request: CreatePostRequest,
        @AuthenticationPrincipal userId: Long
    ): CreatePostResponse

    @Operation(
        summary = "게시글 수정",
        description = """
                    게시글을 수정 합니다."""
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상"),
        ApiResponse(
            responseCode = "400", description = """
                            * 에러코드 
                            - A05001 : 요청한 데이터가 요구사항을 충족하지 않습니다.
                            """,
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        ),
        ApiResponse(
            responseCode = "403",
            description = "권한 없음",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )
    )
    @SecurityRequirement(name = "AccessToken")
    @PutMapping("/{postId}")
    fun editPost(
        @RequestBody @Valid request: EditPostRequest,
        @PathVariable postId: Long,
        @AuthenticationPrincipal userId: Long
    )

    @Operation(
        summary = "게시글 삭제",
        description = """
                    게시글을 삭제 합니다."""
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상"),
        ApiResponse(
            responseCode = "403",
            description = "권한 없음",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )
    )
    @SecurityRequirement(name = "AccessToken")
    @DeleteMapping("/{postId}")
    fun deletePost(
        @PathVariable postId: Long,
        @AuthenticationPrincipal userId: Long
    )

    @Operation(
        summary = "게시글 목록 조회",
        description = """
                    게시글 목록을 조회합니다."""
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상")
    )
    @GetMapping
    fun listPost(
        @Schema(description = """
        검색 키워드 (제목, 글)
        """",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @RequestParam(required = false) keyword : String?,

        @Schema(description = """
        페이징을 위한 토큰
        첫 요청시에는 pageToken null로 요청
        """",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        @RequestParam(required = false) pageToken : String?,

        @Schema(description = """
        페이징할 페이지 사이즈
        0 < pageSize
        """",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @RequestParam(required = true) @Valid @Positive pageSize : Int
    ) : ListPostResponse

    @Operation(
        summary = "게시글 상세 조회",
        description = """
                    게시글 상세를 조회합니다."""
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상")
    )
    @GetMapping("/{postId}")
    fun getPost(@PathVariable postId: Long) : GetPostResponse
}
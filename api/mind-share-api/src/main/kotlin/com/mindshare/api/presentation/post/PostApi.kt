package com.mindshare.api.presentation.post

import com.mindshare.api.core.error.ErrorResponse
import com.mindshare.api.presentation.post.model.request.CreatePostRequest
import com.mindshare.api.presentation.post.model.request.EditPostRequest
import com.mindshare.api.presentation.post.model.response.CreatePostResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping


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
}
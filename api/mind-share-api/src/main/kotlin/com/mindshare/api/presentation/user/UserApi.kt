package com.mindshare.api.presentation.user

import com.mindshare.api.core.web.ErrorResponse
import com.mindshare.api.presentation.user.model.request.ChangeNicknameRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Tag(
    name = "User API",
    description = """
        유저 관련 API 입니다."""
)
@RequestMapping("/user")
interface UserApi {

    @Operation(
        summary = "닉네임 변경",
        description = """
                    닉네임을 변경 합니다."""
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상"), ApiResponse(
            responseCode = "400", description = """
                            * 에러코드 
                            
                            - A01001 : 닉네임 중복
                            - A05001 : 요청한 데이터가 요구사항을 충족하지 않습니다.
                            """,
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )
    )
    @SecurityRequirement(name = "AccessToken")
    @PatchMapping("/nickname")
    fun changeNickname(@RequestBody @Valid request: ChangeNicknameRequest, @AuthenticationPrincipal userId: Long)
}
package com.mindshare.api.presentation.user.model

import com.mindshare.api.application.user.ChangeUserNicknameUseCase
import com.mindshare.api.presentation.user.UserApi
import com.mindshare.api.presentation.user.model.request.ChangeNicknameRequest
import org.springframework.web.bind.annotation.RestController

@RestController
class UserApiController(
    private val changeNicknameUseCase: ChangeUserNicknameUseCase
) : UserApi {

    override fun changeNickname(request: ChangeNicknameRequest, userId: Long) {
        changeNicknameUseCase(request.nickname, userId)
    }
}
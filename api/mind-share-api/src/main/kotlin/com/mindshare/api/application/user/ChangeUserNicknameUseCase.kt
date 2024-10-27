package com.mindshare.api.application.user

import com.mindshare.api.application.auth.error.DuplicateNicknameException
import com.mindshare.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChangeUserNicknameUseCase(
    private val userRepository: UserRepository
) {

    @Transactional
    operator fun invoke(nickname: String, userId : Long) {

        val user = userRepository.findById(userId)
            ?: throw NoSuchElementException("User not found")

        val existsNickname = userRepository.existsByNickname(nickname)
        if (existsNickname) {
            throw DuplicateNicknameException("Nickname already exists")
        }

        user.changeNickname(nickname)
    }
}
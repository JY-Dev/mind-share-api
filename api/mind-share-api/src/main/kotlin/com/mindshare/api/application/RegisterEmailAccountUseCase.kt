package com.mindshare.api.application

import com.mindshare.domain.auth.Account
import com.mindshare.domain.auth.AccountRepository
import com.mindshare.domain.user.User
import com.mindshare.domain.user.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class RegisterEmailAccountUseCase(
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    operator fun invoke(
        nickname : String,
        email : String,
        password: String
    ) {

        val existsNickname = userRepository.existsByNickname(nickname)
        if(existsNickname) throw IllegalStateException("Nickname already exists. nickname : $nickname")

        val user = User(nickname)
        val savedUser = userRepository.save(user)

        val encodedPassword = passwordEncoder.encode(password)
        val account = Account(email, encodedPassword, savedUser.id!!)
        accountRepository.save(account)
    }
}
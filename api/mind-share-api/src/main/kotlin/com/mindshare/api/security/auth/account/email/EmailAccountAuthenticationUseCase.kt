package com.mindshare.api.security.auth.account.email

import com.mindshare.domain.auth.AccountProvider
import com.mindshare.domain.auth.AccountRepository
import com.mindshare.domain.user.User
import com.mindshare.domain.user.UserRepository
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EmailAccountAuthenticationUseCase(
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional(readOnly = true)
    operator fun invoke(email: String, password: String): User {

        val account = accountRepository.findByLoginIdAndAccountProvider(email, AccountProvider.EMAIL)
            ?: throw NoSuchElementException("No account found")

        val sameEmail = account.loginId == email
        val samePassword = passwordEncoder.matches(password, account.credential)

        if (sameEmail.not() || samePassword.not()) {
            throw AuthenticationServiceException("Email or Password not match")
        }

        return userRepository.findById(account.userId)
            ?: throw NoSuchElementException("No user found")
    }
}
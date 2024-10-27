package com.mindshare.api.security.auth.token

import com.mindshare.api.core.log.logger
import com.mindshare.domain.auth.AuthRefreshTokenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class HandleSecurityWarnTokenUseCase(
    private val refreshTokenRepository: AuthRefreshTokenRepository
) {
    private val log = logger()

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    operator fun invoke(userId: Long) {
        log.error("Security Alert: Mismatched User IDs detected. UserId: $userId")
        refreshTokenRepository.findByUserId(userId)?.let(refreshTokenRepository::delete)
    }
}
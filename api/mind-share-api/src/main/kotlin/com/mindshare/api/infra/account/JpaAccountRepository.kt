package com.mindshare.api.infra.account

import com.mindshare.domain.auth.Account
import com.mindshare.domain.auth.AccountRepository
import org.springframework.data.jpa.repository.JpaRepository

interface JpaAccountRepository : JpaRepository<Account, Long>, AccountRepository {
}
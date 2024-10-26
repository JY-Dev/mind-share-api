package com.mindshare.domain.auth

interface AccountRepository {
    fun findByUserId(userid: String): Account?
    fun findByLoginIdAndAccountProvider(loginId: String, accountProvider: AccountProvider): Account?
}
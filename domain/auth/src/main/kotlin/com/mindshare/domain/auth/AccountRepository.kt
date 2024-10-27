package com.mindshare.domain.auth

interface AccountRepository {
    fun save(account: Account) : Account
    fun delete(account: Account)
    fun existsByLoginIdAndAccountProvider(loginId: String, accountProvider: AccountProvider) : Boolean
    fun findByUserId(userid: Long): Account?
    fun findByLoginIdAndAccountProvider(loginId: String, accountProvider: AccountProvider): Account?
}
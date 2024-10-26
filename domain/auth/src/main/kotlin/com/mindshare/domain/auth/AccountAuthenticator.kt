package com.mindshare.domain.auth

interface AccountAuthenticator {
    val accountProvider : AccountProvider

    fun authenticate(loginId : String, credential : String) : Boolean

    fun support(accountProvider: AccountProvider) =
        this.accountProvider == accountProvider
}
package com.mindshare.domain.auth

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AccountTest {

    private lateinit var account: Account
    private val userId = 1L
    private val loginId = "testUser"
    private val credential = "testPassword"
    private val accountProvider = AccountProvider.EMAIL

    @BeforeEach
    fun setup() {
        account = Account(
            loginId = loginId,
            credential = credential,
            userId = userId,
            accountProvider = accountProvider
        )
    }

    @Test
    fun `equals 메서드가 동일한 ID를 가진 객체를 같은 객체로 인식하는지 확인`() {
        // given
        val anotherAccount = Account(
            loginId = "differentLogin",
            credential = "differentCredential",
            userId = userId,
            accountProvider = accountProvider
        )
        anotherAccount.id = account.id // 동일한 ID 설정

        // then
        assertEquals(account, anotherAccount, "동일한 ID를 가진 객체는 같은 객체로 인식되어야 합니다.")
    }

    @Test
    fun `equals 메서드가 ID가 다른 객체를 다른 객체로 인식하는지 확인`() {
        // given
        val anotherAccount = Account(
            loginId = "anotherLogin",
            credential = "anotherCredential",
            userId = 2L,
            accountProvider = AccountProvider.EMAIL,
            id = 2L
        )

        // then
        assertNotEquals(account, anotherAccount, "ID가 다른 객체는 다른 객체로 인식되어야 합니다.")
    }

    @Test
    fun `hashCode 메서드가 동일한 ID를 가진 객체에서 동일한 해시코드를 반환하는지 확인`() {
        // given
        val anotherAccount = Account(
            loginId = "differentLogin",
            credential = "differentCredential",
            userId = userId,
            accountProvider = accountProvider
        )
        anotherAccount.id = account.id // 동일한 ID 설정

        // then
        assertEquals(account.hashCode(), anotherAccount.hashCode(), "동일한 ID를 가진 객체는 동일한 해시코드를 반환해야 합니다.")
    }

}
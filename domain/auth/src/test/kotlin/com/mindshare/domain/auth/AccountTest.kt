package com.mindshare.domain.auth

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito

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

    @Test
    fun `authenticate 메서드가 올바른 인증기로 성공적인 인증을 수행하는지 확인`() {
        // given
        val authenticator = Mockito.mock(AccountAuthenticator::class.java).apply {
            Mockito.`when`(this.accountProvider).thenReturn(AccountProvider.EMAIL)
            Mockito.`when`(this.support(AccountProvider.EMAIL)).thenReturn(true)
            Mockito.`when`(this.authenticate(loginId, credential)).thenReturn(true)
        }

        // when & then
        assertDoesNotThrow {
            account.authenticate(listOf(authenticator))
        }
    }

    @Test
    fun `authenticate 메서드가 인증기를 찾지 못하면 예외를 발생시키는지 확인`() {
        // given
        val unsupportedAuthenticator = Mockito.mock(AccountAuthenticator::class.java).apply {
            Mockito.`when`(this.accountProvider).thenReturn(AccountProvider.EMAIL)
            Mockito.`when`(this.support(AccountProvider.EMAIL)).thenReturn(false)
        }

        // when & then
        val exception = assertThrows<IllegalArgumentException> {
            account.authenticate(listOf(unsupportedAuthenticator))
        }
        assertEquals("Account not supported. Provider : $accountProvider", exception.message)
    }

    @Test
    fun `authenticate 메서드가 인증 실패 시 예외를 발생시키는지 확인`() {
        // given
        val authenticator = Mockito.mock(AccountAuthenticator::class.java).apply {
            Mockito.`when`(this.accountProvider).thenReturn(AccountProvider.EMAIL)
            Mockito.`when`(this.support(AccountProvider.EMAIL)).thenReturn(true)
            Mockito.`when`(this.authenticate(loginId, credential)).thenReturn(false)
        }

        // when & then
        val exception = assertThrows<IllegalStateException> {
            account.authenticate(listOf(authenticator))
        }
        assertEquals("Account not authenticated. Authenticator: $authenticator", exception.message)
    }
}
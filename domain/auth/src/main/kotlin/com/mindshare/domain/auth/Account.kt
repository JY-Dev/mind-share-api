package com.mindshare.domain.auth

import com.mindshare.core.jpa.TimeAuditableEntity
import jakarta.persistence.*

@Entity
@Table(name = "ACCOUNT")
class Account(

    loginId: String,

    credential: String,

    @Column(name = "USER_ID")
    val userId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "ACCOUNT_PROVIDER")
    val accountProvider: AccountProvider = AccountProvider.EMAIL,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long? = null

) : TimeAuditableEntity() {


    @Column(name = "LOGIN_ID")
    var loginId: String = loginId
        private set

    @Column(name = "CREDENTIAL")
    var credential: String = credential
        private set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Account) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
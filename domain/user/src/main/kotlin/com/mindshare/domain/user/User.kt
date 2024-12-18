package com.mindshare.domain.user

import com.mindshare.core.jpa.TimeAuditableEntity
import jakarta.persistence.*

@Entity
@Table(name = "USER")
class User(
    nickname : String,
    userType: UserType = UserType.USER,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long? = null
) : TimeAuditableEntity() {

    @Column(name = "NICKNAME")
    var nickname: String = nickname
        private set

    @Column(name = "USER_TYPE")
    @Enumerated(EnumType.STRING)
    var userType: UserType = userType
    private set

    fun changeNickname(nickname: String) {
        this.nickname = nickname
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
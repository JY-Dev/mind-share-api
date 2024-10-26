package com.mindshare.domain.user

import com.mindshare.core.jpa.TimeAuditableEntity
import jakarta.persistence.*

@Entity
@Table(name = "USER")
class User(
    nickname : String,
    userType: UserType = UserType.USER,
) : TimeAuditableEntity() {

    @Column(name = "NICKNAME")
    var nickname: String = nickname
        private set

    @Column(name = "USER_TYPE")
    var userType: UserType = userType
    private set

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long? = null
        private set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
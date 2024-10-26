package com.mindshare.domain.auth

import com.mindshare.core.jpa.TimeAuditableEntity
import com.mindshare.core.jpa.converter.InstantToUtcConverter
import jakarta.persistence.*
import java.time.Duration
import java.time.Instant
import java.util.*

@Entity
@Table(name = "AUTH_REFRESH_TOKEN")
class AuthRefreshToken(

    @Column(name = "USER_ID", unique = true)
    val userId: Long,

    @Column(name = "SESSION_ID")
    private val sessionId: String = UUID.randomUUID().toString(),

    @Column(name = "EXPIRATION_TIME")
    @Convert(converter = InstantToUtcConverter::class)
    private var expirationTime: Instant = Instant.now(),


) : TimeAuditableEntity() {

    @Column(name = "TOKEN", unique = true)
    var token: String = UUID.randomUUID().toString()
    private set

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long? = null
    private set

    fun isExpired(now: Instant): Boolean =
        expirationTime.isBefore(now)

    fun rotationToken(expirationDuration: Duration) {
        this.token = UUID.randomUUID().toString()
        this.expirationTime = Instant.now()
            .plus(expirationDuration)
    }

    fun isSameSession(sessionId: String) =
        this.sessionId == sessionId

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AuthRefreshToken) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
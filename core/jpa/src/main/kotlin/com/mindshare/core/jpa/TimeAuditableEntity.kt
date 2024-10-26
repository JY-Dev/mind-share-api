package com.mindshare.core.jpa

import com.mindshare.core.jpa.converter.InstantToUtcConverter
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@MappedSuperclass
@EntityListeners(value = [AuditingEntityListener::class])
abstract class TimeAuditableEntity(
    @CreatedDate
    @Column(name = "CT_UTC")
    @Convert(converter = InstantToUtcConverter::class)
    protected var creationTime: Instant = Instant.now(),

    @LastModifiedDate
    @Column(name = "UT_UTC")
    @Convert(converter = InstantToUtcConverter::class)
    protected var updateTime: Instant = Instant.now()
) {

}


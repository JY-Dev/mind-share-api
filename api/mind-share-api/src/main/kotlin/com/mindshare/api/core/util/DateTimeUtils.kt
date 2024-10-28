package com.mindshare.api.core.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun Instant.toKstLocalDateTime(): LocalDateTime {
    return this.atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime()
}
package com.mindshare.api.core.util

import org.apache.commons.codec.binary.Base64
import org.springframework.util.Assert
import java.nio.charset.StandardCharsets
import java.time.Instant

private const val PAGE_TOKEN_FORMAT = "{}|{}"

fun <T, R> Pair<T, R>.toPageToken(): String {
    val formatted = PAGE_TOKEN_FORMAT.format(first.toStringValue(), second.toStringValue())
    return Base64.encodeBase64URLSafeString(formatted.toByteArray(StandardCharsets.UTF_8))
}

inline fun <reified T, reified R> String.toPairPageToken(): Pair<T, R> {
    val decoded = String(Base64.decodeBase64(this), StandardCharsets.UTF_8)
    val parts = decoded.split("|", limit = 2)
    Assert.isTrue(parts.size == 2, "Invalid pageToken")
    return parts[0].toTypeValue<T>() to parts[1].toTypeValue<R>()
}

inline fun <reified T> String.toTypeValue(): T = when (T::class) {
    String::class -> this as T
    Int::class -> this.toInt() as T
    Long::class -> this.toLong() as T
    Boolean::class -> this.toBoolean() as T
    Instant::class -> Instant.ofEpochMilli(this.toLong()) as T
    else -> throw IllegalArgumentException("Unsupported type - type: ${T::class}")
}

private fun <T> T.toStringValue(): String = when (this) {
    is Instant -> this.toEpochMilli().toString()
    else -> this.toString()
}
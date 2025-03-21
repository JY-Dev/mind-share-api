package com.mindshare.api.core.error

open class BusinessAuthorizationException(
    message: String,
    val errorCode: ErrorCode
) : RuntimeException(message) {
}
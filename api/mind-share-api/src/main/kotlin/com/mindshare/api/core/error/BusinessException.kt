package com.mindshare.api.core.error

open class BusinessException(
    message : String,
    val errorCode: ErrorCode
) : RuntimeException(message) {
}
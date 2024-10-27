package com.mindshare.api.core.error

data class ErrorResponse(
    val message: String,
    val errorCode: ErrorCode
)

package com.mindshare.api.core.web

import com.mindshare.api.core.error.ErrorCode

data class ErrorResponse(
    val message: String,
    val errorCode: ErrorCode
)

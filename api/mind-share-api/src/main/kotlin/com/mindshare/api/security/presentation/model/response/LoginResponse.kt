package com.mindshare.api.security.presentation.model.response

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
)

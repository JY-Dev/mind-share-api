package com.mindshare.api.security.auth.token

data class AuthToken(
    val accessToken: String,
    val refreshToken: String
)

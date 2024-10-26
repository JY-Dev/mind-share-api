package com.mindshare.api.security.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import java.time.Duration

@ConfigurationProperties(prefix = "auth")
data class AuthProperties @ConstructorBinding constructor(
    val secretKey: String,
    val accessTokenTtl: Duration,
    val refreshTokenTtl: Duration
)
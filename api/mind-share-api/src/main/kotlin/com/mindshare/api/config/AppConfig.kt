package com.mindshare.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.mindshare.api.security.config.AuthProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AuthProperties::class)
class AppConfig {
    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().registerKotlinModule()
    }
}
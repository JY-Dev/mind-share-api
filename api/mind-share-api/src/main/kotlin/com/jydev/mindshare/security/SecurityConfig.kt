package com.jydev.mindshare.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher

@Configuration
class SecurityConfig {

    @Bean
    fun ignoredMatchers(): OrRequestMatcher {
        return OrRequestMatcher(
            AntPathRequestMatcher("/v3/api-docs/**"),
            AntPathRequestMatcher("/swagger-ui/**")
        )
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { c ->
            c.disable()
        }.authorizeHttpRequests { registry ->
            registry
                .requestMatchers(ignoredMatchers()).permitAll()
                .anyRequest().authenticated()
        }
        return http.build()
    }

}
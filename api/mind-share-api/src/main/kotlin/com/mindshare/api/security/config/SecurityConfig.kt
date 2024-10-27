package com.mindshare.api.security.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.mindshare.api.security.auth.UserAuthenticationFailHandler
import com.mindshare.api.security.auth.UserAuthenticationSuccessHandler
import com.mindshare.api.security.auth.account.AccountAuthenticationConverter
import com.mindshare.api.security.auth.account.email.EmailAccountAuthenticationUseCase
import com.mindshare.api.security.auth.account.email.EmailAccountAuthenticationProvider
import com.mindshare.api.security.auth.token.AuthTokenUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationConverter
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationFilter
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher

@Configuration
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder =
        BCryptPasswordEncoder()

    @Bean
    fun ignoredMatchers(): OrRequestMatcher {
        return OrRequestMatcher(
            AntPathRequestMatcher("/v3/api-docs/**"),
            AntPathRequestMatcher("/swagger-ui/**"),
            AntPathRequestMatcher("/account/signup/**"),
            AntPathRequestMatcher("/auth/login/**"),
        )
    }

    @Bean
    fun filterChain(http: HttpSecurity, loginAuthenticationFilter: AuthenticationFilter): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .authorizeHttpRequests { authRequests ->
                authRequests
                    .requestMatchers(ignoredMatchers()).permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(loginAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun loginAuthenticationFilter(
        loginAuthenticationManger: AuthenticationManager,
        loginAuthenticationConvertor: AuthenticationConverter,
        loginSuccessHandler: AuthenticationSuccessHandler,
        loginFailHandler : AuthenticationFailureHandler,
    ): AuthenticationFilter = AuthenticationFilter(loginAuthenticationManger, loginAuthenticationConvertor).apply {
        this.requestMatcher = AntPathRequestMatcher(LOGIN_PATH)
        this.successHandler = loginSuccessHandler
        this.failureHandler = loginFailHandler
    }

    @Bean
    fun loginSuccessHandler(
        authTokenUseCase: AuthTokenUseCase,
        objectMapper: ObjectMapper,
    ): AuthenticationSuccessHandler {
        return UserAuthenticationSuccessHandler(authTokenUseCase, objectMapper)
    }

    @Bean
    fun loginFailHandler(
        objectMapper: ObjectMapper
    ): AuthenticationFailureHandler {
        return UserAuthenticationFailHandler(objectMapper)
    }

    @Bean
    fun loginAuthenticationConvertor(objectMapper: ObjectMapper): AuthenticationConverter =
        AccountAuthenticationConverter(objectMapper)

    @Bean
    fun loginAuthenticationManger(
        emailAuthenticationProvider: AuthenticationProvider
    ): AuthenticationManager {
        val providerList = listOf(emailAuthenticationProvider)
        return ProviderManager(providerList)
    }

    @Bean
    fun emailAuthenticationProvider(authenticationUseCase: EmailAccountAuthenticationUseCase): AuthenticationProvider {
        return EmailAccountAuthenticationProvider(authenticationUseCase)
    }

    companion object {
        const val LOGIN_PATH = "/auth/login/**"
    }

}
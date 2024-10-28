package com.mindshare.api.security.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.mindshare.api.core.log.logger
import com.mindshare.api.security.SecurityErrorCodeAuthenticationEntryPoint
import com.mindshare.api.security.auth.UserAuthenticationSuccessHandler
import com.mindshare.api.security.auth.account.AccountAuthenticationConverter
import com.mindshare.api.security.auth.account.email.EmailAccountAuthenticationProvider
import com.mindshare.api.security.auth.account.email.EmailAccountAuthenticationUseCase
import com.mindshare.api.security.auth.token.AuthTokenAuthenticationConverter
import com.mindshare.api.security.auth.token.AuthTokenUseCase
import com.mindshare.api.security.auth.token.JwtAuthenticationProvider
import com.mindshare.api.security.auth.token.JwtHelper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.*
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.NegatedRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher

@Configuration
class SecurityConfig {

    private val log = logger()

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun ignoredMatchers(): OrRequestMatcher {
        return OrRequestMatcher(
            AntPathRequestMatcher("/v3/api-docs/**"),
            AntPathRequestMatcher("/swagger-ui/**"),
            AntPathRequestMatcher("/account/signup/**"),
            AntPathRequestMatcher("/auth/login/**"),
            AntPathRequestMatcher("/auth/issue-token"),
            AntPathRequestMatcher("/posts", HttpMethod.GET.name()),
            AntPathRequestMatcher("/posts/*", HttpMethod.GET.name())
        )
    }

    @Bean
    fun filterChain(
        http: HttpSecurity,
        authenticationEntryPoint: AuthenticationEntryPoint,
        authTokenAuthenticationFilter: AuthenticationFilter,
        loginAuthenticationFilter: AuthenticationFilter
    ): SecurityFilterChain {
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
            .addFilterBefore(authTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(loginAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling { exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(authenticationEntryPoint)
            }
            .build()
    }

    @Bean
    fun loginAuthenticationFilter(
        loginAuthenticationUseCase: EmailAccountAuthenticationUseCase,
        objectMapper: ObjectMapper,
        authenticationEntryPoint: AuthenticationEntryPoint,
        authTokenUseCase: AuthTokenUseCase,
    ): AuthenticationFilter {
        val loginAuthenticationManager = ProviderManager(listOf(EmailAccountAuthenticationProvider(loginAuthenticationUseCase)))
        val loginAuthenticationConverter = AccountAuthenticationConverter(objectMapper)

        return AuthenticationFilter(loginAuthenticationManager, loginAuthenticationConverter).apply {
            requestMatcher = AntPathRequestMatcher(LOGIN_PATH)
            successHandler = UserAuthenticationSuccessHandler(authTokenUseCase, objectMapper)
            failureHandler = AuthenticationEntryPointFailureHandler(authenticationEntryPoint)
        }
    }

    @Bean
    fun authTokenAuthenticationFilter(
        jwtHelper: JwtHelper,
        authenticationEntryPoint: AuthenticationEntryPoint,
    ): AuthenticationFilter {
        val jwtAuthenticationProvider = JwtAuthenticationProvider(jwtHelper)
        val authTokenAuthenticationManager = ProviderManager(listOf(jwtAuthenticationProvider))
        val authTokenAuthenticationConverter = AuthTokenAuthenticationConverter()

        return AuthenticationFilter(authTokenAuthenticationManager, authTokenAuthenticationConverter).apply {
            requestMatcher = NegatedRequestMatcher(ignoredMatchers())
            successHandler = AuthenticationSuccessHandler { _, _, _ -> log.debug("Authentication successful") }
            failureHandler = AuthenticationEntryPointFailureHandler(authenticationEntryPoint)
        }
    }

    @Bean
    fun authenticationEntryPoint(objectMapper: ObjectMapper): AuthenticationEntryPoint {
        return SecurityErrorCodeAuthenticationEntryPoint(objectMapper)
    }

    companion object {
        const val LOGIN_PATH = "/auth/login/**"
    }

}
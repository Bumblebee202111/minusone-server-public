package com.github.bumblebee202111.minusone.server.config

import com.github.bumblebee202111.minusone.server.filter.ApiAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    @Order(0)
    fun publicResourcesFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            securityMatcher(
                AntPathRequestMatcher("/songs/**"),
            )
            authorizeHttpRequests {
                authorize(anyRequest, permitAll)
            }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            csrf { disable() }
        }
        return http.build()
    }

    @Bean
    @Order(1)
    fun apiSecurityFilterChain(http: HttpSecurity, apiAuthFilter: ApiAuthFilter): SecurityFilterChain {
        http {
            securityMatcher("/api/**", "/eapi/**")
            authorizeHttpRequests {
                authorize(anyRequest, permitAll)
            }
            csrf { disable() }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(apiAuthFilter)
        }
        return http.build()
    }

    @Bean
    @Order(2)
    fun adminSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            securityMatcher("/admin/**")
            authorizeHttpRequests {
                authorize(anyRequest, authenticated)
            }
            httpBasic { }
            csrf { disable() }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
        }
        return http.build()
    }

    @Bean
    fun userDetailsService(passwordEncoder: PasswordEncoder): UserDetailsService {
        val user = User.withUsername("admin")
            .password(passwordEncoder.encode("password"))
            .roles("ADMIN")
            .build()
        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
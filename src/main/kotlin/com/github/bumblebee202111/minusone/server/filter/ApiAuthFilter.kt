package com.github.bumblebee202111.minusone.server.filter

import com.github.bumblebee202111.minusone.server.constant.api.ApiConstants
import com.github.bumblebee202111.minusone.server.service.api.AuthService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class ApiAuthFilter(
    private val authService: AuthService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val cookies = parseCookies(request)
        val musicUCookie = cookies[ApiConstants.COOKIE_MUSIC_U]
        val musicACookie = cookies[ApiConstants.COOKIE_MUSIC_A]


        var isAuthenticatedUser = false

        if (musicUCookie != null) {
            val account = authService.validateTokenAndGetAccount(musicUCookie)
            if (account != null) {
                request.setAttribute(ApiConstants.ATTR_USER_ACCOUNT, account)
                val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
                val authentication =
                    UsernamePasswordAuthenticationToken(account.userName, null, authorities)
                SecurityContextHolder.getContext().authentication = authentication
                isAuthenticatedUser = true
            } else {
                val expiredCookie = Cookie(ApiConstants.COOKIE_MUSIC_U, "").apply { maxAge = 0; path = "/" }
                response.addCookie(expiredCookie)
                SecurityContextHolder.clearContext()
            }


        }
        if (!isAuthenticatedUser && musicACookie != null) {
            request.setAttribute(ApiConstants.COOKIE_MUSIC_A, musicACookie)
        }

        try {
            chain.doFilter(request, response)
        } finally {
            SecurityContextHolder.clearContext()
        }
    }

    private fun parseCookies(request: HttpServletRequest): Map<String, String> {
        return request.cookies?.associate { it.name to it.value } ?: emptyMap()
    }
}
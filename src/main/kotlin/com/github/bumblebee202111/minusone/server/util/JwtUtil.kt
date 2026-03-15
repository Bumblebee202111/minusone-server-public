package com.github.bumblebee202111.minusone.server.util

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtil {
    private val log = LoggerFactory.getLogger(JwtUtil::class.java)

    @Value("\${app.jwt.secret:default-secret-key-that-is-very-long-and-secure-1234567890}")
    private lateinit var secret: String

    @Value("\${app.jwt.expiration-ms:86400000}")
    private var expirationTime: Long = 86400000L 

    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray())
    }

    fun generateToken(username: String): String {
        return Jwts.builder()
            .subject(username)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(key)
            .compact()
    }

    fun extractUsername(token: String): String? {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
                .subject
        } catch (e: JwtException) {
            log.debug("Invalid JWT token: {}", e.message)
            null
        } catch (e: IllegalArgumentException) {
            log.debug("JWT claims string is empty: {}", e.message)
            null
        }
    }
}
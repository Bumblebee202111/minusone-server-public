package com.github.bumblebee202111.minusone.server.repository

import com.github.bumblebee202111.minusone.server.entity.AuthToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface AuthTokenRepository:JpaRepository<AuthToken,String> {
    fun findByTokenAndExpiresAtAfter(token: String, now: Instant): AuthToken?

    fun deleteAllByExpiresAtBefore(now: Instant): Long
}
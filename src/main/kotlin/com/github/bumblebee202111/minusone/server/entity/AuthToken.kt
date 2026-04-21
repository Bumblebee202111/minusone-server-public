package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "auth_tokens")
data class AuthToken(
    @Id
    @Column(
        length = 1024,
        columnDefinition = "VARCHAR(1024) CHARACTER SET ascii COLLATE ascii_general_ci"
    )
    val token: String,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    var account: Account,

    @Column(nullable = false)
    var expiresAt: Instant
)

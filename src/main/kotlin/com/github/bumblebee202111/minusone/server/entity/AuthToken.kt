package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
data class AuthToken(
    @Id
    val token:String,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    var account: com.github.bumblebee202111.minusone.server.entity.Account,

    @Column(nullable = false)
    var expiresAt: Instant
)

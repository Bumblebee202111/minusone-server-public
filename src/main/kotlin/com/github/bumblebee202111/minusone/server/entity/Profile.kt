package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.*

@Entity
@Table(name = "profiles")
data class Profile(
    @Id
    val userId: Long = 0,
    val avatarUrl: String = DEFAULT_AVATAR_URL,
    val nickname: String,
    val backgroundUrl: String? = null,
    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id")
    val account: Account,
) {
    companion object {
        @Suppress("HttpUrlsUsage")
        const val DEFAULT_AVATAR_URL = "http:
    }
}

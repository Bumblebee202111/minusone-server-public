package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.*

@Entity
data class User(
    @Id
    val id: Long = 0L,
    val nickname: String,
    val createTime: Long,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "id", referencedColumnName = "id")
    val account: Account,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "id", referencedColumnName = "id")
    val profile: Profile,

    val listenSongs: Long = 0L,
)

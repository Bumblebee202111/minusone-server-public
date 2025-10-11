package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.*

@Entity
data class User(
    @Id
    val id:Long=0L,
    val nickname:String,
    val createTime:Long,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "id", referencedColumnName = "id")
    val account: com.github.bumblebee202111.minusone.server.entity.Account,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "id", referencedColumnName = "id")
    val profile: com.github.bumblebee202111.minusone.server.entity.Profile,

    val listenSongs:Long=0L,

    )

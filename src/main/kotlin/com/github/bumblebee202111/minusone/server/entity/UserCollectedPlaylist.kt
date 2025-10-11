package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.*

@Entity
data class UserCollectedPlaylist(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long=0,
    val userId: Long,

    @OneToOne
    @JoinColumn(name = "playlist_id", referencedColumnName = "id")
    val playlist: com.github.bumblebee202111.minusone.server.entity.Playlist
)

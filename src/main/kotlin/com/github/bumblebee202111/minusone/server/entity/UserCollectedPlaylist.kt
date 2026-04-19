package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.*

@Entity
@Table(name = "user_collected_playlists")
data class UserCollectedPlaylist(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id")
    val userId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", referencedColumnName = "id")
    val playlist: Playlist
)

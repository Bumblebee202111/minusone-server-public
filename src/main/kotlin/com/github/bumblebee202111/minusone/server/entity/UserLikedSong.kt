package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.*

@Entity
data class UserLikedSong(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long=0,
    val userId:Long,
    @OneToOne
    @JoinColumn(name = "song_id", referencedColumnName = "id")
    val song: com.github.bumblebee202111.minusone.server.entity.Song
)
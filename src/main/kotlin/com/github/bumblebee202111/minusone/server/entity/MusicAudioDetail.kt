package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.*

@Entity
data class MusicAudioDetail(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "song_id", unique = true)
    val songId: Long,

    



)
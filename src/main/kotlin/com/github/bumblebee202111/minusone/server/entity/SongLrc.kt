package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.*

@Entity
@Table(name = "song_lrcs")
data class SongLrc(
    @Id
    val id: Long,

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "song_id")
    val song: Song,

    @Lob
    val lyric: String?
)
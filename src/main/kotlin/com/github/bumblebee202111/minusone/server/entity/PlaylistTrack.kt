package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.*

@Entity
@Table(name = "playlist_tracks")
data class PlaylistTrack(
    @EmbeddedId
    val id: PlaylistTrackKey,
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("playlistId")
    @JoinColumn(name = "playlist_id")
    val playlist: Playlist,
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("songId")
    @JoinColumn(name = "song_id")
    val song: Song,
    
)
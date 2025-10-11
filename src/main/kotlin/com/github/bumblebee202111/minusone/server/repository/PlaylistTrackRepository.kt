package com.github.bumblebee202111.minusone.server.repository

import com.github.bumblebee202111.minusone.server.entity.Playlist
import com.github.bumblebee202111.minusone.server.entity.PlaylistTrack
import com.github.bumblebee202111.minusone.server.entity.PlaylistTrackKey
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PlaylistTrackRepository:JpaRepository<PlaylistTrack,PlaylistTrackKey> {
    @Query("SELECT pt FROM PlaylistTrack pt JOIN FETCH pt.song s WHERE pt.playlist.id = :playlistId")
    fun findByPlaylistIdWithSongs(playlistId: Long, pageable: Pageable): List<PlaylistTrack>

    @Query("SELECT pt.song.id FROM PlaylistTrack pt WHERE pt.playlist.id = :playlistId")
    fun findSongIdsByPlaylistId(playlistId: Long): List<Long>

    @Query("SELECT pt.song.id FROM PlaylistTrack pt WHERE pt.playlist.id = :playlistId")
    fun findSongIdsByPlaylistId(playlistId: Long, pageable: Pageable): List<Long>

    @Query("SELECT count(pt) FROM PlaylistTrack pt WHERE pt.song.id = :songId AND pt.playlist.specialType = :specialType")
    fun countLikesForSong(songId: Long, specialType: Playlist.SpecialType = Playlist.SpecialType.STAR): Long
}
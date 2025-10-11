package com.github.bumblebee202111.minusone.server.repository

import com.github.bumblebee202111.minusone.server.entity.Playlist
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlaylistRepository:JpaRepository<Playlist,Long> {

    fun findByUserIdAndSpecialType(userId:Long,specialType: Playlist.SpecialType): Playlist

    fun findAllByUserId(userId:Long):List<Playlist>

    fun findBySpecialTypeAndUserId(specialType: Playlist.SpecialType, userId: Long): Playlist?
}
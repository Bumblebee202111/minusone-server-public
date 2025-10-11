package com.github.bumblebee202111.minusone.server.repository

import com.github.bumblebee202111.minusone.server.entity.Playlist
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlaylistRepository:JpaRepository<com.github.bumblebee202111.minusone.server.entity.Playlist,Long> {

    fun findByUserIdAndSpecialType(userId:Long,specialType: com.github.bumblebee202111.minusone.server.entity.Playlist.SpecialType): com.github.bumblebee202111.minusone.server.entity.Playlist

    fun findAllByUserId(userId:Long):List<com.github.bumblebee202111.minusone.server.entity.Playlist>

    fun findBySpecialTypeAndUserId(specialType: com.github.bumblebee202111.minusone.server.entity.Playlist.SpecialType, userId: Long): com.github.bumblebee202111.minusone.server.entity.Playlist?
}
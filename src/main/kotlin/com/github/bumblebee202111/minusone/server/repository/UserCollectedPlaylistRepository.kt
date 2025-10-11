package com.github.bumblebee202111.minusone.server.repository

import com.github.bumblebee202111.minusone.server.entity.UserCollectedPlaylist
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserCollectedPlaylistRepository:JpaRepository<com.github.bumblebee202111.minusone.server.entity.UserCollectedPlaylist,Long> {
    fun findUserCollectedPlaylistsByUserId(userId:Long):List<com.github.bumblebee202111.minusone.server.entity.UserCollectedPlaylist>
}
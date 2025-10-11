package com.github.bumblebee202111.minusone.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Suppress("FunctionName")
@Repository
interface UserLikedSongRepository:JpaRepository<com.github.bumblebee202111.minusone.server.entity.UserLikedSong,Long> {
    fun findAllByUserId(userId:Long):List<com.github.bumblebee202111.minusone.server.entity.UserLikedSong>

    fun countUserLikedSongsBySong_Id(songId:Long):Long

    fun deleteUserLikedSongByUserIdAndSongId(userId: Long,songId: Long)
}
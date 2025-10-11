package com.github.bumblebee202111.minusone.server.repository

import com.github.bumblebee202111.minusone.server.entity.UserLikedSong
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Suppress("FunctionName")
@Repository
interface UserLikedSongRepository:JpaRepository<UserLikedSong,Long> {
    fun findAllByUserId(userId:Long):List<UserLikedSong>

    fun countUserLikedSongsBySong_Id(songId:Long):Long

    fun deleteUserLikedSongByUserIdAndSongId(userId: Long,songId: Long)
}
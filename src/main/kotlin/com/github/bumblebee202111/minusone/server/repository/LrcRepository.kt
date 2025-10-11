package com.github.bumblebee202111.minusone.server.repository

import com.github.bumblebee202111.minusone.server.entity.SongLrc
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LrcRepository : JpaRepository<SongLrc, Long> {
    fun findBySongId(songId: Long): SongLrc?
}
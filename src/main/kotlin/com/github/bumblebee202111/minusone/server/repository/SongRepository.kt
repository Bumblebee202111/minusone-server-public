package com.github.bumblebee202111.minusone.server.repository

import com.github.bumblebee202111.minusone.server.entity.Song
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SongRepository:JpaRepository<Song,Long> {
    fun findAllByIdIn(ids: Collection<Long>): List<Song>
}
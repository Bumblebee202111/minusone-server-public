package com.github.bumblebee202111.minusone.server.dto.api.response

import com.github.bumblebee202111.minusone.server.entity.Song
import jakarta.persistence.Entity
import jakarta.persistence.Id

data class SongDto(
    val id: Long,
    val name:String?,
    val fee:Int=0
)

fun com.github.bumblebee202111.minusone.server.entity.Song.toDto()= SongDto(id = id, name = name)
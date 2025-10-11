package com.github.bumblebee202111.minusone.server.dto.api.response

import com.github.bumblebee202111.minusone.server.entity.Song

data class SongDto(
    val id: Long,
    val name: String?,
    val fee: Int = 0
)

fun Song.toDto() = SongDto(id = id, name = name)
package com.github.bumblebee202111.minusone.server.dto.admin.response

import com.github.bumblebee202111.minusone.server.entity.Song

data class AdminSongDto(
    val id: Long,
    val name: String,
    val fee: Int = 0
)

fun Song.toAdminSongDto() = AdminSongDto(id = id, name = name)
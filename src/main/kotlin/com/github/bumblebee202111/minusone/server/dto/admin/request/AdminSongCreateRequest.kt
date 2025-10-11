package com.github.bumblebee202111.minusone.server.dto.admin.request

import com.github.bumblebee202111.minusone.server.entity.Song
import jakarta.validation.constraints.NotBlank

data class AdminSongCreateRequest(
    @NotBlank(message = "Song name cannot be blank")
    val name: String
)

fun AdminSongCreateRequest.toEntity(): com.github.bumblebee202111.minusone.server.entity.Song =
    com.github.bumblebee202111.minusone.server.entity.Song(
        name = name
    )

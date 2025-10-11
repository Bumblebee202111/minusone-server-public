package com.github.bumblebee202111.minusone.server.dto.admin.request

import com.github.bumblebee202111.minusone.server.entity.AudioDetail
import com.github.bumblebee202111.minusone.server.entity.Song
import jakarta.validation.constraints.Size

data class AdminSongUpdateRequest(
    @Size(min = 1, message = "Song name cannot be empty if provided")
    val name: String?,
    @Size(min = 1, message = "File path cannot be empty if provided")
    val filePath: String?,
    val h: com.github.bumblebee202111.minusone.server.entity.AudioDetail?,
    val m: com.github.bumblebee202111.minusone.server.entity.AudioDetail?,
    val l: com.github.bumblebee202111.minusone.server.entity.AudioDetail?,
    val sq: com.github.bumblebee202111.minusone.server.entity.AudioDetail?
)

fun AdminSongUpdateRequest.updateEntity(song: com.github.bumblebee202111.minusone.server.entity.Song): com.github.bumblebee202111.minusone.server.entity.Song {
    return song.apply {
        this@updateEntity.name?.let { newName ->
            this.name = newName
        }
        this@updateEntity.filePath?.let { newFilePath ->
            this.filePath = newFilePath
        }
    }
}

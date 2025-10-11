package com.github.bumblebee202111.minusone.server.dto.api.response

import com.github.bumblebee202111.minusone.server.entity.Album

data class AlbumDto(
    val id:Long,
    val name:String
)


fun Album.toDto(): AlbumDto {
    return AlbumDto(id = id, name = name)
}

package com.github.bumblebee202111.minusone.server.dto.api.response

data class SubbedAlbumListDto(
    val data: List<AlbumDto>,
    val count: Long,
    val hasMore: Boolean
)
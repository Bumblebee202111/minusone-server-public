package com.github.bumblebee202111.minusone.server.dto.api.response

import com.github.bumblebee202111.minusone.server.entity.Playlist

data class PlaylistDto(
    val id: Long,
    val name: String,
    val userId: Long,
    val tracks: List<SongDto>,
    val trackIds: List<TrackIdDto>
)

fun Playlist.toDto(songDtos: List<SongDto>, trackIds: List<TrackIdDto>): PlaylistDto {
    return PlaylistDto(id = id, name = name, userId = userId, tracks = songDtos, trackIds = trackIds)
}

data class TrackIdDto(
    val id: Long,
    val v: Int = 0
)
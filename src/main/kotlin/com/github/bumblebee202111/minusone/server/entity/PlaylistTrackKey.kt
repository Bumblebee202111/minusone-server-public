package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class PlaylistTrackKey(
    val playlistId:Long,
    val songId:Long
):Serializable

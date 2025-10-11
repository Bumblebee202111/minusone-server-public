package com.github.bumblebee202111.minusone.server.dto.api.internal

import com.github.bumblebee202111.minusone.server.dto.api.response.PrivilegeDto
import com.github.bumblebee202111.minusone.server.dto.api.response.PlaylistDto

data class PlaylistDetailResult(
    val playlist: PlaylistDto,
    val privileges: List<PrivilegeDto>
)
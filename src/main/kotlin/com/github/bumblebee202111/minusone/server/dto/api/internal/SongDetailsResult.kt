package com.github.bumblebee202111.minusone.server.dto.api.internal

import com.github.bumblebee202111.minusone.server.dto.api.response.PrivilegeDto
import com.github.bumblebee202111.minusone.server.dto.api.response.SongDto

data class SongDetailsResult(
    val songs: List<SongDto>,
    val privileges: List<PrivilegeDto>
)
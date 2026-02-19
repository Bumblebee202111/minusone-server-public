package com.github.bumblebee202111.minusone.server.dto.api.response

import com.github.bumblebee202111.minusone.server.entity.Profile

data class V1UserDetailDto(
    val listenSongs:Long=0,
    val profile: Profile
)
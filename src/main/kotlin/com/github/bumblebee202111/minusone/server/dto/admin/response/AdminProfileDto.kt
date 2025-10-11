package com.github.bumblebee202111.minusone.server.dto.admin.response

import com.github.bumblebee202111.minusone.server.entity.Profile

data class AdminProfileDto(
    val userId: Long=0,
    val nickname: String,
    val avatarUrl: String,
    val backgroundUrl: String?
)

fun com.github.bumblebee202111.minusone.server.entity.Profile.toAdminProfileDto()= AdminProfileDto(
    userId = userId,
    nickname = nickname,
    avatarUrl = avatarUrl,
    backgroundUrl= backgroundUrl
)
package com.github.bumblebee202111.minusone.server.dto.api.response

import com.github.bumblebee202111.minusone.server.entity.Profile

data class ProfileDto(
    val userId: Long=0,
    val avatarUrl: String,
    val backgroundUrl: String?
)
fun Profile.toDto(): ProfileDto {
    return ProfileDto(
        userId = userId,
        avatarUrl = avatarUrl,
        backgroundUrl = backgroundUrl
    )
}
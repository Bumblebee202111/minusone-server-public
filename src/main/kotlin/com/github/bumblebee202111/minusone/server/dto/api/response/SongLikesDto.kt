package com.github.bumblebee202111.minusone.server.dto.api.response

data class SongLikesDto(
    private val ids:List<Long>,
    val checkPoint:Long
)
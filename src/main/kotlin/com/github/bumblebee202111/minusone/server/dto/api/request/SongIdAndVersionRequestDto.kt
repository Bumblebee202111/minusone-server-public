package com.github.bumblebee202111.minusone.server.dto.api.request

import com.fasterxml.jackson.annotation.JsonProperty

data class SongIdAndVersionRequestDto(
    val id: Long,
    @JsonProperty("v")
    val version: Int = 0
)
package com.github.bumblebee202111.minusone.server.dto.api.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class LyricsDto(
    val sgc: Boolean = false,
    val sfy: Boolean = false,
    val qfy: Boolean = false,
    val lrc: LyricDto?,
    val klyric: LyricDto? = null,
    val tlyric: LyricDto? = null,
    val romalrc: LyricDto? = null
)
package com.github.bumblebee202111.minusone.server.dto.api.response

data class SongUrlDto(
    val id: Long,
    val url: String?,
    val br: Int,
    val size: Long,
    val md5: String?,
    val fee: Int=0,
    val type: String?,  
    val code: Int,      
    val expi: Int       
)
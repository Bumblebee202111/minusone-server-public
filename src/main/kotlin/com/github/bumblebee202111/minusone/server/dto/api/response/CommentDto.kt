package com.github.bumblebee202111.minusone.server.dto.api.response

import com.github.bumblebee202111.minusone.server.entity.Comment
import com.github.bumblebee202111.minusone.server.entity.Profile

data class CommentDto(
    val user: ProfileDto,
    val commentId: Long,
    val content: String,
    val time:Long,
    val timeStr: String,
    val likedCount: Int = 0,
    val liked: Boolean = false
)

fun Comment.toDto(profile: Profile) =
    CommentDto(
        user = profile.toDto(),
        commentId = id,
        content = content,
        time = this.createTime.toEpochMilli(),
        timeStr = "...", 
    )
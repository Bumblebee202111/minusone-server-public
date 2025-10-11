package com.github.bumblebee202111.minusone.server.dto.api.response

data class CommentResponseDto(
    val commentsTitle: String,
    val totalCount: Long,
    val hasMore: Boolean,
    val comments: List<CommentDto>,
    val sortTypeList: List<SortTypeDto>,
    val cursor: String,
    val sortType: Int
)

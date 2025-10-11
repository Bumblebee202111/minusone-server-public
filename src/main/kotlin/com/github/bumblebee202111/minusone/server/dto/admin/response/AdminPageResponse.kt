package com.github.bumblebee202111.minusone.server.dto.admin.response

import org.springframework.data.domain.Page

data class AdminPageResponse<T>(
    val data: List<T>,
    val totalCount: Int
)

inline fun <reified T, reified R> Page<T>.toAdminPageResponse(mapItemToDto: (T) -> R) = AdminPageResponse(
    data = content.map { mapItemToDto(it) },
    totalCount = totalElements.toInt()
)
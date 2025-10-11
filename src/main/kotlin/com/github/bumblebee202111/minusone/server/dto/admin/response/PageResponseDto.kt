package com.github.bumblebee202111.minusone.server.dto.admin.response

import org.springframework.data.domain.Page

data class PageResponseDto<T>(
    val content: List<T>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalElements: Long,
    val totalPages: Int,
    val isLast: Boolean
) {
    companion object {
        fun <T> from(page: Page<T>): PageResponseDto<T> {
            return PageResponseDto(
                content = page.content,
                pageNumber = page.number,
                pageSize = page.size,
                totalElements = page.totalElements,
                totalPages = page.totalPages,
                isLast = page.isLast
            )
        }
    }
}
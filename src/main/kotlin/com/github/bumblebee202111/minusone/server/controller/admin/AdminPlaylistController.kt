package com.github.bumblebee202111.minusone.server.controller.admin

import com.github.bumblebee202111.minusone.server.dto.admin.response.PageResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/playlists")
class AdminPlaylistController {

    @GetMapping
    fun listPlaylists(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): PageResponseDto<Any> {
        
        return PageResponseDto(
            content = emptyList(),
            pageNumber = page,
            pageSize = size,
            totalElements = 0,
            totalPages = 0,
            isLast = true
        )
    }
}
package com.github.bumblebee202111.minusone.server.controller.admin

import com.github.bumblebee202111.minusone.server.dto.admin.request.AdminSongUpdateRequest
import com.github.bumblebee202111.minusone.server.dto.admin.request.AdminSongCreateRequest
import com.github.bumblebee202111.minusone.server.service.admin.AdminSongService
import com.github.bumblebee202111.minusone.server.dto.admin.response.AdminSongDto
import com.github.bumblebee202111.minusone.server.dto.admin.response.PageResponseDto
import com.github.bumblebee202111.minusone.server.dto.api.response.SongDto
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/songs")
class AdminSongController(private val songService: AdminSongService) {
    @PostMapping
    fun createSong(@Valid @RequestBody request: AdminSongCreateRequest): AdminSongDto {
        return songService.createSong(request)
    }

    @GetMapping
    fun listSongs(
        @RequestParam(defaultValue = "0") @PositiveOrZero page: Int,
        @RequestParam(defaultValue = "20") @Positive size: Int
    ): PageResponseDto<AdminSongDto> {
        return songService.listSongs(page, size)
    }

    @GetMapping("/{songId}")
    fun getSong(@PathVariable @Positive songId: Long): AdminSongDto {
        return songService.getSong(songId)
    }


    @PutMapping("/{songId}")
    fun updateSong(@PathVariable songId: Long, @Valid @RequestBody request: AdminSongUpdateRequest): AdminSongDto {
        return songService.updateSong(songId, request)
    }

    @DeleteMapping("/{songId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSong(@PathVariable @Positive songId: Long) {
        songService.deleteSong(songId)
    }
}
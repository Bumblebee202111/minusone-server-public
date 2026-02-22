package com.github.bumblebee202111.minusone.server.controller.admin

import com.github.bumblebee202111.minusone.server.dto.admin.request.AdminSongUpdateRequest
import com.github.bumblebee202111.minusone.server.dto.admin.request.AdminSongCreateRequest
import com.github.bumblebee202111.minusone.server.service.admin.AdminSongService
import com.github.bumblebee202111.minusone.server.dto.admin.response.AdminSongDto
import com.github.bumblebee202111.minusone.server.dto.admin.response.PageResponseDto
import com.github.bumblebee202111.minusone.server.dto.api.response.SongDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/songs")
@Tag(name = "Admin Song", description = "Admin Song related APIs")
class AdminSongController(private val songService: AdminSongService) {
    @PostMapping
    @Operation(summary = "Create song", description = "Creates a new song")
    fun createSong(@Valid @RequestBody request: AdminSongCreateRequest): AdminSongDto {
        return songService.createSong(request)
    }

    @GetMapping
    @Operation(summary = "List songs", description = "Lists songs with pagination")
    fun listSongs(
        @RequestParam(defaultValue = "0") @PositiveOrZero page: Int,
        @RequestParam(defaultValue = "20") @Positive size: Int
    ): PageResponseDto<AdminSongDto> {
        return songService.listSongs(page, size)
    }

    @GetMapping("/{songId}")
    @Operation(summary = "Get song", description = "Gets a song by ID")
    fun getSong(@PathVariable @Positive songId: Long): AdminSongDto {
        return songService.getSong(songId)
    }


    @PutMapping("/{songId}")
    @Operation(summary = "Update song", description = "Updates a song by ID")
    fun updateSong(@PathVariable songId: Long, @Valid @RequestBody request: AdminSongUpdateRequest): AdminSongDto {
        return songService.updateSong(songId, request)
    }

    @DeleteMapping("/{songId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete song", description = "Deletes a song by ID")
    fun deleteSong(@PathVariable @Positive songId: Long) {
        songService.deleteSong(songId)
    }
}

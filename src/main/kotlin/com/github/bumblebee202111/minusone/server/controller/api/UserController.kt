package com.github.bumblebee202111.minusone.server.controller.api

import com.github.bumblebee202111.minusone.server.service.api.UserService
import com.github.bumblebee202111.minusone.server.dto.api.response.ApiResponse
import com.github.bumblebee202111.minusone.server.dto.api.response.UserPlaylistsDto
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UserController(val userService: UserService) {

    @RequestMapping("/v1/user/detail/{uid}")
    fun getV1UserDetail(@PathVariable @Positive uid: Long): ApiResponse {
        val targetAccount = userService.getAccountById(uid)
        val targetProfile = if (targetAccount != null) userService.getProfileByAccountId(uid) else null
        if (targetAccount == null || targetProfile == null) {
            return ApiResponse.error(404, "用户不存在")
        }
        val listenSongsPlaceholder = 0
        return ApiResponse.success().apply {
            add("profile", targetProfile)
            add("listenSongs", listenSongsPlaceholder)
        }
    }

    @RequestMapping("/user/playlist")
    fun userPlaylists(
        @RequestParam @Positive uid: Long,
        @RequestParam @PositiveOrZero limit: Int = 1000,
        @RequestParam @PositiveOrZero offset: Int = 0
    ): ApiResponse {
        val playlists = userService.userPlaylists(uid)
        return ApiResponse.success().apply {
            add("playlist", playlists)
        }
    }

}
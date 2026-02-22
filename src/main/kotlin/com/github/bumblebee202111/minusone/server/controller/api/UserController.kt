package com.github.bumblebee202111.minusone.server.controller.api

import com.github.bumblebee202111.minusone.server.service.api.UserService
import com.github.bumblebee202111.minusone.server.dto.api.response.ApiResponse
import com.github.bumblebee202111.minusone.server.dto.api.response.UserPlaylistsDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
@Tag(name = "User", description = "User related APIs")
class UserController(val userService: UserService) {

    @RequestMapping("/v1/user/detail/{uid}")
    @Operation(summary = "Get user detail", description = "Gets user detail by user ID")
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
    @Operation(summary = "Get user playlists", description = "Gets user playlists by user ID")
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

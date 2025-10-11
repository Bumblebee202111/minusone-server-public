package com.github.bumblebee202111.minusone.server.controller.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.bumblebee202111.minusone.server.constant.api.ApiConstants
import com.github.bumblebee202111.minusone.server.service.api.MusicService
import com.github.bumblebee202111.minusone.server.dto.api.request.SongIdAndVersionRequestDto
import com.github.bumblebee202111.minusone.server.dto.api.response.*
import com.github.bumblebee202111.minusone.server.entity.Account
import com.github.bumblebee202111.minusone.server.exception.api.LogoutException
import com.github.bumblebee202111.minusone.server.exception.api.WrrongParamException
import com.github.bumblebee202111.minusone.server.resolver.JsonQueryParam
import com.github.bumblebee202111.minusone.server.service.api.UserMusicService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import org.hibernate.validator.constraints.Range
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api")
class MusicController(
    private val musicService: MusicService,
    private val userMusicService: UserMusicService,
    private val objectMapper: ObjectMapper
) {

    
    @RequestMapping(path = ["/playlist/v4/detail", "/v6/playlist/detail"])
    fun getPlaylistDetail(
        @RequestParam @Positive id: Long,
        @RequestParam(defaultValue = "0") @PositiveOrZero trackUpdateTime: Long,
        @RequestParam(defaultValue = "1000") @Range(min = 0, max = 1000) n: Int,
        @PositiveOrZero
        @RequestParam(defaultValue = "5") s: Int,
        @RequestParam(defaultValue = "0") shareUserId: String 
    ): ApiResponse {
        val result = musicService.getPlaylistDetail(id, n)

        return ApiResponse.success().apply {
            add("playlist", result.playlist)
            add("privileges", result.privileges)
        }
    }

    @RequestMapping("/v3/song/detail")
    fun getV3SongDetails(@JsonQueryParam("c") songRequests: List<SongIdAndVersionRequestDto>): ApiResponse {
        val songIds = songRequests.map { it.id }
        if (songIds.isEmpty()) {
            throw WrrongParamException()
        }
        val (songs, privileges) = musicService.getSongDetails(songIds)
        return ApiResponse.success().apply {
            add("songs", songs)
            add("privileges", privileges)
        }
    }

    @RequestMapping("/song/enhance/player/url/v1")
    fun getSongUrlsV1(
        @JsonQueryParam("ids") songIds: List<Long>?,
        @RequestParam("br", required = false) br: Int?
    ): ApiResponse {
        if (songIds.isNullOrEmpty()) {
            throw WrrongParamException()
        }

        val songUrls = musicService.getSongUrls(songIds)

        return ApiResponse.success(data = songUrls)
    }

    @RequestMapping("/song/lyric/v1")
    fun getSongLyricsV1(@RequestParam @Positive id: Long): ApiResponse {
        val lyrics = musicService.getSongLyrics(id)
        return ApiResponse.successFlattened(dataToFlatten = lyrics, objectMapper = objectMapper)
    }

    @GetMapping("/red/count")
    fun getSongRedCount(@RequestParam @Positive songId: Long): ApiResponse {
        val redHeartResult = musicService.getSongRedCount(songId)
        return ApiResponse.success(data = redHeartResult)
    }

    @RequestMapping("/song/like")
    fun likeSong(
        @RequestParam("like") like: Boolean,
        @RequestParam("trackId") trackId: Long,
        request: HttpServletRequest
    ): ApiResponse {
        val account = request.getAttribute(ApiConstants.ATTR_USER_ACCOUNT) as? Account
            ?: throw LogoutException()

        val likedPlaylistId = userMusicService.likeSong(account, trackId, like)

        return ApiResponse.success().apply {
            add("playlistId", likedPlaylistId)
        }
    }

    @GetMapping("/album/sublist")
    fun getAlbumSublist(
        @RequestParam(defaultValue = "25") @Positive limit: Int,
        @RequestParam(defaultValue = "0") @PositiveOrZero offset: Int,
        request: HttpServletRequest
    ): ApiResponse {
        val account = request.getAttribute(ApiConstants.ATTR_USER_ACCOUNT) as? Account
            ?: throw LogoutException()

        val subbedAlbumList = userMusicService.getSubbedAlbums(account, limit, offset)

        return ApiResponse.success(data = subbedAlbumList)
    }

    @RequestMapping("/playlist/privilege")
    fun getPlaylistPrivileges(
        @Positive id: Long,
        @RequestParam(defaultValue = "1000") @Range(min = 0, max = 1000) n: Int
    ): ApiResponse {
        val privileges = musicService.getPlaylistPrivileges(id, n)
        return ApiResponse.success(privileges)
    }

    @RequestMapping("song/like/get")
    fun getSongLikes(@CookieValue(value = "MUSIC_U", defaultValue = "") musicUCookieValue: String): SongLikesDto {
        return musicService.getLikedSongs(musicUCookieValue)
    }

}
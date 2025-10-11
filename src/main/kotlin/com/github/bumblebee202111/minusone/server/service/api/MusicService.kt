package com.github.bumblebee202111.minusone.server.service.api

import com.github.bumblebee202111.minusone.server.dto.admin.response.RedHeartResultDto
import com.github.bumblebee202111.minusone.server.dto.api.response.PrivilegeDto
import com.github.bumblebee202111.minusone.server.dto.api.internal.PlaylistDetailResult
import com.github.bumblebee202111.minusone.server.dto.api.internal.SongDetailsResult
import com.github.bumblebee202111.minusone.server.dto.api.response.*
import com.github.bumblebee202111.minusone.server.dto.api.response.toDto
import com.github.bumblebee202111.minusone.server.exception.admin.ResourceNotFoundException
import com.github.bumblebee202111.minusone.server.exception.api.LogoutException
import com.github.bumblebee202111.minusone.server.repository.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MusicService(
    private val songRepository: SongRepository,
    private val playlistRepository: PlaylistRepository,
    private val lrcRepository: LrcRepository,
    private val userLikedSongRepository: UserLikedSongRepository,
    private val authTokenRepository: AuthTokenRepository,
    private val playlistTrackRepository: PlaylistTrackRepository,
    @Value("\${app.media.delivery-url}")
    private val cdnBaseUrl: String
) {
    fun getPlaylistDetail(id: Long, n: Int): PlaylistDetailResult {
        val playlist = playlistRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Playlist with ID $id not found.") }
        val pageable = PageRequest.of(0, n)
        val playlistTracks = playlistTrackRepository.findByPlaylistIdWithSongs(playlist.id, pageable)
        val songDtos = playlistTracks.map { it.song.toDto() }

        val songIds: List<Long> = playlistTrackRepository.findSongIdsByPlaylistId(playlist.id)

        val trackIdDtos = songIds.map { songId ->
            TrackIdDto(id = songId)
        }

        val privileges = songDtos.map { PrivilegeDto.createPlaceholder(it.id) }

        val playlistDto = playlist.toDto(songDtos, trackIdDtos)

        return PlaylistDetailResult(
            playlistDto, privileges
        )
    }

    fun getSongDetails(ids: List<Long>): SongDetailsResult {
        val foundSongs = songRepository.findAllById(ids)
        val songDtos = foundSongs.map { it.toDto() }
        val privilegeDtos = foundSongs.map { PrivilegeDto.createPlaceholder(it.id) }
        return SongDetailsResult(songDtos, privilegeDtos)
    }

    @Transactional(readOnly = true)
    fun getSongUrls(songIds: List<Long>): List<SongUrlDto> {
        val foundSongs = songRepository.findAllById(songIds).associateBy { it.id }

        return songIds.map { songId ->
            val song = foundSongs[songId]
            val filePath = song?.filePath
            if (filePath.isNullOrBlank()) {
                SongUrlDto(
                    id = songId,
                    url = null,
                    br = 0,
                    size = 0,
                    md5 = null,
                    fee = 0,
                    type = null,
                    code = 404,
                    expi = 120
                )
            } else {
                SongUrlDto(
                    id = songId,
                    url = "$cdnBaseUrl/${filePath}",
                    br = 320000,
                    size = 5242880,
                    md5 = "placeholder_md5_hash",
                    type = filePath.substringAfterLast('.', "mp3"),
                    code = 200,
                    expi = 120
                )
            }
        }
    }

    @Transactional(readOnly = true)
    fun getSongLyrics(songId: Long): LyricsDto {
        val lrcEntity = lrcRepository.findBySongId(songId)
        val lrcDto = LyricDto(lyric = lrcEntity?.lyric)
        return LyricsDto(lrc = lrcDto)
    }

    @Transactional(readOnly = true)
    fun getSongRedCount(songId: Long): RedHeartResultDto {
        if (!songRepository.existsById(songId)) {
            return RedHeartResultDto(count = 0, countDesc = "0")
        }

        val likeCount = playlistTrackRepository.countLikesForSong(songId)

        return RedHeartResultDto(
            count = likeCount,
            countDesc = likeCount.toString()
        )
    }

    @Transactional(readOnly = true)
    fun getPlaylistPrivileges(id: Long, n: Int): List<PrivilegeDto> {
        if (!playlistRepository.existsById(id)) {
            throw ResourceNotFoundException("Playlist with ID $id not found.")
        }

        val pageable = PageRequest.of(0, n)
        val songIds = playlistTrackRepository.findSongIdsByPlaylistId(id, pageable)

        val privileges = songIds.map { songId ->
            PrivilegeDto.createPlaceholder(songId)
        }

        return privileges
    }

    fun getLikedSongs(token: String): SongLikesDto {
        val userId = findUserIdOrThrow(token)
        val songIds = userLikedSongRepository.findAllByUserId(userId).map { it.song.toDto().id }
        return SongLikesDto(songIds, System.currentTimeMillis())
    }


    private fun findUserIdOrThrow(token: String): Long {
        val userToken = token.takeIf(String::isNotBlank)?.let {
            authTokenRepository.findByIdOrNull(it)
        } ?: throw LogoutException()
        return userToken.account.id
    }

}

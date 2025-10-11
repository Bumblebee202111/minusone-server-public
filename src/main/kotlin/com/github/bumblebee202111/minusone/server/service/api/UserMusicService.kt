package com.github.bumblebee202111.minusone.server.service.api

import com.github.bumblebee202111.minusone.server.dto.api.response.SubbedAlbumListDto
import com.github.bumblebee202111.minusone.server.dto.api.response.toDto
import com.github.bumblebee202111.minusone.server.entity.Account
import com.github.bumblebee202111.minusone.server.entity.Playlist
import com.github.bumblebee202111.minusone.server.entity.PlaylistTrack
import com.github.bumblebee202111.minusone.server.entity.PlaylistTrackKey
import com.github.bumblebee202111.minusone.server.exception.api.NotFoundException
import com.github.bumblebee202111.minusone.server.repository.PlaylistRepository
import com.github.bumblebee202111.minusone.server.repository.PlaylistTrackRepository
import com.github.bumblebee202111.minusone.server.repository.SongRepository
import com.github.bumblebee202111.minusone.server.repository.UserSubbedAlbumRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserMusicService(
    private val songRepository: SongRepository,
    private val playlistRepository: PlaylistRepository,
    private val playlistTrackRepository: PlaylistTrackRepository,
    private val subbedAlbumRepository: UserSubbedAlbumRepository
) {
    
    @Transactional
    fun likeSong(account: Account, songId: Long, like: Boolean): Long {
        val likedPlaylist = playlistRepository.findByUserIdAndSpecialType(account.id, Playlist.SpecialType.STAR)

        val trackKey = PlaylistTrackKey(playlistId = likedPlaylist.id, songId = songId)

        if (like) {
            if (!playlistTrackRepository.existsById(trackKey)) {
                if (!songRepository.existsById(songId)) {
                    throw NotFoundException()
                }
                val songReference = songRepository.getReferenceById(songId)
                val playlistReference = playlistRepository.getReferenceById(likedPlaylist.id)

                val newTrack = PlaylistTrack(
                    id = trackKey,
                    playlist = playlistReference,
                    song = songReference
                )
                playlistTrackRepository.save(newTrack)
            }
        } else {
            playlistTrackRepository.deleteById(trackKey)
        }
        return likedPlaylist.id
    }

    @Transactional(readOnly = true)
    fun getSubbedAlbums(account: Account, limit: Int, offset: Int): SubbedAlbumListDto {
        val pageNumber = if (limit > 0) offset / limit else 0
        val pageable = PageRequest.of(pageNumber, limit)

        val subbedAlbumPage = subbedAlbumRepository.findByAccountIdWithAlbum(account.id, pageable)

        val albumDtos = subbedAlbumPage.content.map { it.album.toDto() }

        return SubbedAlbumListDto(
            data = albumDtos,
            count = subbedAlbumPage.totalElements,
            hasMore = subbedAlbumPage.hasNext()
        )
    }
}
package com.github.bumblebee202111.minusone.server.service.api

import com.github.bumblebee202111.minusone.server.entity.*
import com.github.bumblebee202111.minusone.server.repository.PlaylistRepository
import com.github.bumblebee202111.minusone.server.repository.PlaylistTrackRepository
import com.github.bumblebee202111.minusone.server.repository.SongRepository
import com.github.bumblebee202111.minusone.server.repository.UserSubbedAlbumRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class UserMusicServiceTest {
    @MockK
    lateinit var songRepository: SongRepository

    @MockK
    lateinit var playlistRepository: PlaylistRepository

    @MockK
    lateinit var playlistTrackRepository: PlaylistTrackRepository

    @MockK
    lateinit var subbedAlbumRepository: UserSubbedAlbumRepository

    @InjectMockKs
    private lateinit var userMusicService: UserMusicService

    @Test
    fun `when liking a song that is not liked yet, it should be added to the playlist`() {
        val userAccount = Account(
            id = 1L,
            userName = "1_1234567890",
            phone = "1234567890",
            password = "md5_hash_placeholder"
        )
        val songToLike = Song(
            id = 101L,
            name = "Test Song"
        )
        val starPlaylist = Playlist(
            id = 99L,
            name = "My Liked Songs",
            specialType = Playlist.SpecialType.STAR,
            userId = 1L
        )
        val trackKey = PlaylistTrackKey(playlistId = 99L, songId = 101L)
        val newPlaylistTrack = PlaylistTrack(id = trackKey, playlist = starPlaylist, song = songToLike)

        every { songRepository.existsById(101L) } returns true
        every { playlistRepository.findByUserIdAndSpecialType(1L, Playlist.SpecialType.STAR) } returns starPlaylist
        every { playlistTrackRepository.existsById(trackKey) } returns false
        every { songRepository.getReferenceById(101L) } returns songToLike
        every { playlistRepository.getReferenceById(99L) } returns starPlaylist
        every { playlistTrackRepository.save(any()) } returns newPlaylistTrack

        val resultPlaylistId = userMusicService.likeSong(account = userAccount, songId = 101L, like = true)

        assertEquals(99L, resultPlaylistId)

        verify(exactly = 1) { playlistTrackRepository.save(any()) }
        verify(exactly = 0) { playlistTrackRepository.deleteById(any()) }
    }

    @Test
    fun `when unliking a song that is already liked, it should be deleted`() {
        val userAccount = Account(
            id = 1L,
            userName = "1_1234567890",
            phone = "1234567890",
            password = "md5_hash_placeholder"
        )
        val starPlaylist = Playlist(
            id = 99L,
            name = "My Liked Songs",
            specialType = Playlist.SpecialType.STAR,
            userId = 1L
        )
        val trackKey = PlaylistTrackKey(playlistId = 99L, songId = 101L)

        every { songRepository.existsById(101L) } returns true
        every { playlistRepository.findByUserIdAndSpecialType(1L, Playlist.SpecialType.STAR) } returns starPlaylist

        every { playlistTrackRepository.deleteById(trackKey) } just runs

        val resultPlaylistId = userMusicService.likeSong(account = userAccount, songId = 101L, like = false)

        assertEquals(99L, resultPlaylistId)

        verify(exactly = 1) { playlistTrackRepository.deleteById(trackKey) }
        verify(exactly = 0) { playlistTrackRepository.save(any()) }
    }

}
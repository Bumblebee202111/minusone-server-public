package com.github.bumblebee202111.minusone.server.service.api

import com.github.bumblebee202111.minusone.server.dto.api.response.PlaylistDto
import com.github.bumblebee202111.minusone.server.dto.api.response.toDto
import com.github.bumblebee202111.minusone.server.entity.Account
import com.github.bumblebee202111.minusone.server.entity.Profile
import com.github.bumblebee202111.minusone.server.entity.UserCollectedPlaylist
import com.github.bumblebee202111.minusone.server.repository.AccountRepository
import com.github.bumblebee202111.minusone.server.repository.PlaylistRepository
import com.github.bumblebee202111.minusone.server.repository.ProfileRepository
import com.github.bumblebee202111.minusone.server.repository.UserCollectedPlaylistRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userCollectedPlaylistRepository: UserCollectedPlaylistRepository,
    private val playlistRepository: PlaylistRepository,
    private val accountRepository: AccountRepository,
    private val profileRepository: ProfileRepository
) {
    fun userPlaylists(uid: Long): List<PlaylistDto> {
        val created = playlistRepository.findAllByUserId(uid)
        val collected = userCollectedPlaylistRepository.findUserCollectedPlaylistsByUserId(uid).map(
            UserCollectedPlaylist::playlist
        )
        return (created + collected).map { it.toDto(emptyList(), emptyList()) }
    }

    fun getAccountById(uid: Long): Account? {
        return accountRepository.findByIdOrNull(uid)
    }

    fun getProfileByAccountId(uid: Long): Profile? {
        return profileRepository.findByIdOrNull(uid)
    }
}
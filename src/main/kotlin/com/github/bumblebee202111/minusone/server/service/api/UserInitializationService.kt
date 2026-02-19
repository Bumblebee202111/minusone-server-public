package com.github.bumblebee202111.minusone.server.service.api

import com.github.bumblebee202111.minusone.server.entity.Account
import com.github.bumblebee202111.minusone.server.entity.Playlist
import com.github.bumblebee202111.minusone.server.entity.Profile
import com.github.bumblebee202111.minusone.server.repository.PlaylistRepository
import com.github.bumblebee202111.minusone.server.repository.ProfileRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserInitializationService(
    private val profileRepository: ProfileRepository,
    private val playlistRepository: PlaylistRepository
) {
    private val log = LoggerFactory.getLogger(UserInitializationService::class.java)

    @Transactional
    fun initializeNewUser(account: Account, nickname: String): Profile {
        val profile = createProfile(account, nickname)
        createLikedSongsPlaylist(account)
        return profile
    }

    private fun createProfile(account: Account, nickname: String): Profile {
        val newProfile = Profile(
            account = account,
            nickname = nickname
        )
        val savedProfile = profileRepository.save(newProfile)
        log.info("Created profile for accountId: {}", account.id)
        return savedProfile
    }

    private fun createLikedSongsPlaylist(account: Account) {
        try {
            val likeList = Playlist(
                name = "喜欢的音乐",
                specialType = Playlist.SpecialType.STAR,
                userId = account.id,
            )
            playlistRepository.save(likeList)
            log.info("Created '喜欢的音乐' playlist for accountId: {}", account.id)
        } catch (e: Exception) {
            log.error(
                "CRITICAL: Failed to create '喜欢的音乐' playlist for accountId: {}. Error: {}",
                account.id, e.message, e
            )
            throw RuntimeException("Failed to initialize crucial user data (favorite playlist).", e)
        }
    }
}

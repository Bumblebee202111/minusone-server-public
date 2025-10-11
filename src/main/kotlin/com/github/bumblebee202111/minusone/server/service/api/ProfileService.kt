package com.github.bumblebee202111.minusone.server.service.api

import com.github.bumblebee202111.minusone.server.entity.Profile
import com.github.bumblebee202111.minusone.server.repository.ProfileRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileService(
    private val profileRepository: ProfileRepository
) {
    private val log = LoggerFactory.getLogger(ProfileService::class.java)

    @Transactional(readOnly = true)
    fun findByAccountId(accountId: Long): Profile? {
        val profile = profileRepository.findById(accountId).orElse(null)
        if (profile == null) {
            log.warn("No profile found for accountId: {}", accountId)
        }
        return profile
    }

}
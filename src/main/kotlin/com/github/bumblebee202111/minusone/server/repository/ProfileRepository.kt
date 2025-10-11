package com.github.bumblebee202111.minusone.server.repository

import com.github.bumblebee202111.minusone.server.entity.Account
import com.github.bumblebee202111.minusone.server.entity.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfileRepository: JpaRepository<Profile, Long> {
    fun findByAccount(account: Account): Profile?

    fun findAllByUserIdIn(userIds: List<Long>): List<Profile>
}
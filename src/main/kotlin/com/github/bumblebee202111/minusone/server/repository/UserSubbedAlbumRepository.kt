package com.github.bumblebee202111.minusone.server.repository

import com.github.bumblebee202111.minusone.server.entity.SubbedAlbum
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserSubbedAlbumRepository:JpaRepository<SubbedAlbum,Long> {
    @Query(
        value = "SELECT sa FROM SubbedAlbum sa JOIN FETCH sa.album a WHERE sa.account.id = :accountId ORDER BY sa.subbedAt DESC",
        countQuery = "SELECT count(sa) FROM SubbedAlbum sa WHERE sa.account.id = :accountId"
    )
    fun findByAccountIdWithAlbum(accountId: Long, pageable: Pageable): Page<SubbedAlbum>
}
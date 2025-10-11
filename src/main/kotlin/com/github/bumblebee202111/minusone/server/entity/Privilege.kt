package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id


@Entity
data class Privilege(
    @Id
    val id: Long,
    val pl: Int,
    val dl: Int,
)
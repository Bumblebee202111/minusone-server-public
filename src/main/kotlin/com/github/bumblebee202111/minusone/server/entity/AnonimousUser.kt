package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "anonimous_users")
data class AnonimousUser(
    @Id
    val userId: Long,
    val createTime: Long
)
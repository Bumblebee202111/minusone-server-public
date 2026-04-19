package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "anonimous_users")
data class AnonimousUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Long = 0L,
    val createTime: Long
)
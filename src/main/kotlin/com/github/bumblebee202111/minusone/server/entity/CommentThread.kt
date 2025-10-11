package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.*

@Entity
data class CommentThread(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(unique = true, nullable = false)
    var resourceId: String
)

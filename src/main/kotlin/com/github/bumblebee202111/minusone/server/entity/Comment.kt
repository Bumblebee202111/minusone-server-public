package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "comments")
data class Comment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "thread_id")
    var thread: CommentThread,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    var user: Account,

    @Lob
    val content: String,
    var likedCount: Int = 0,

    @Column(nullable = false, updatable = false)
    var createTime: Instant = Instant.now()
)

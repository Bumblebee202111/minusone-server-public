package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "subbed_albums", uniqueConstraints = [UniqueConstraint(columnNames = ["account_id", "album_id"])])
data class SubbedAlbum(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id")
    val account: Account,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "album_id")
    val album: Album,

    @Column(nullable = false, updatable = false)
    val subbedAt: Instant = Instant.now()
)
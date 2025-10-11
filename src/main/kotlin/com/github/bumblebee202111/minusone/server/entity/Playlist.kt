package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.*

@Entity
data class Playlist(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long=0,
    val name: String,
    @Enumerated(EnumType.STRING)
    val specialType: SpecialType,
    val userId:Long,
    @OneToMany(mappedBy = "playlist")
    val tracks:List<PlaylistTrack> = emptyList()
){
    enum class SpecialType(value: Int) {
        COLLECT(-30),
        NORMAL(0),
        STAR(5)
    }
}
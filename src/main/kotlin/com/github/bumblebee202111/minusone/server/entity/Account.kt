package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.*

@Entity
@Table(name = "accounts")
data class Account(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long= 0,
    @Column(unique = true, nullable = false)
    val userName: String,
    @Column(nullable = false)
    var phone: String,
    @Column(nullable = false)
    var password: String,  

    
    val status:Int=0,
    val type:Int=1,
)

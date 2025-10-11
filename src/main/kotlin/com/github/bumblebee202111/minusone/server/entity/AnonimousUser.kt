package com.github.bumblebee202111.minusone.server.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class AnonimousUser(
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    val userId:Long=0L,
    val createTime:Long
)
package com.github.bumblebee202111.minusone.server.repository

import com.github.bumblebee202111.minusone.server.entity.AnonimousUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AnonimousUserRepository:JpaRepository<com.github.bumblebee202111.minusone.server.entity.AnonimousUser,Long> {

}
package com.github.bumblebee202111.minusone.server.repository

import com.github.bumblebee202111.minusone.server.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository:JpaRepository<Account,Long> {
    fun existsByPhone(phone:String):Boolean

    fun findByUserNameAndPassword(phone:String, password:String): Account?

    fun existsByUserName(userName: String): Boolean

    fun findByUserName(userName: String): Account?

    fun findByPhone(phone: String): Account?
}
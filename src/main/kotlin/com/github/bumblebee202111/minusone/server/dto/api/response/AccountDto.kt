package com.github.bumblebee202111.minusone.server.dto.api.response

import com.github.bumblebee202111.minusone.server.entity.Account

data class AccountDto(
    val id: Long,
    val userName: String,
    val status: Int = 0,
    val type: Int = 1,
    val phone: String?
)

fun Account.toDto(): AccountDto {
    return AccountDto(
        id = this.id,
        userName = this.userName,
        phone = this.phone
    )
}

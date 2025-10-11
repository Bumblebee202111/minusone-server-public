package com.github.bumblebee202111.minusone.server.dto.admin.response

import com.github.bumblebee202111.minusone.server.entity.Account

data class AdminAccountDto(
    val id: Long,
    val userName: String,
    val phone: String,
)

fun com.github.bumblebee202111.minusone.server.entity.Account.toAdminAccountDto(): AdminAccountDto = AdminAccountDto(
    id = id, userName = userName, phone = phone
)



package com.github.bumblebee202111.minusone.server.dto.admin.response

import com.github.bumblebee202111.minusone.server.entity.Account
import com.github.bumblebee202111.minusone.server.entity.Profile

data class AdminUserCreateResponseDto(
    val account: AdminAccountDto,
    val profile: AdminProfileDto
)

fun Pair<Account, Profile>.toAdminUserResponse(): AdminUserCreateResponseDto {
    val (account, profile) = this
    return AdminUserCreateResponseDto(account = account.toAdminAccountDto(), profile = profile.toAdminProfileDto())
}
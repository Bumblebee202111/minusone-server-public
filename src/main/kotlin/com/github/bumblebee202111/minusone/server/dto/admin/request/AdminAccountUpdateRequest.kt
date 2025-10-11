package com.github.bumblebee202111.minusone.server.dto.admin.request

import com.github.bumblebee202111.minusone.server.entity.Account
import com.github.bumblebee202111.minusone.server.util.CryptoUtil
import jakarta.validation.constraints.Size

data class AdminAccountUpdateRequest(
    @Size(min = 6, max = 12, message = "Password must be at least 6 characters long")
    val password: String
)

fun AdminAccountUpdateRequest.updateEntity(account: com.github.bumblebee202111.minusone.server.entity.Account): com.github.bumblebee202111.minusone.server.entity.Account {
    return account.apply {
        this.password = CryptoUtil.md5(this@updateEntity.password)
    }
}
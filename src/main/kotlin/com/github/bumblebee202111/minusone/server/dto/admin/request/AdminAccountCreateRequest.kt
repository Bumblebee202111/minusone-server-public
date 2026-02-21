package com.github.bumblebee202111.minusone.server.dto.admin.request

import com.github.bumblebee202111.minusone.server.entity.Account
import com.github.bumblebee202111.minusone.server.entity.Profile
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class AdminAccountCreateRequest(
    @NotBlank(message = "Phone number is required")
    @Size(min = 5, max = 15, message = "Phone number length must be between 5 and 15")
    val phone: String,

    val countrycode: String = "86",

    @NotBlank(message = "Nickname is required")
    @Size(min = 2, max = 30, message = "Nickname length must be between 2 and 30")
    val nickname: String,

    @NotBlank(message = "Password (MD5) is required")
    @Size(min = 32, max = 32, message = "Password (MD5) must be 32 characters long")
    val password: String
)

fun AdminAccountCreateRequest.toAccountEntity(): Account {
    return Account(
        phone = this.phone,
        userName = "1_${this.phone}",
        password = this.password
    )
}

fun AdminAccountCreateRequest.toProfileEntity(associatedAccount: Account): Profile {
    return Profile(
        account = associatedAccount,
        nickname = this.nickname
    )
}

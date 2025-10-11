package com.github.bumblebee202111.minusone.server.dto.api.response

import com.github.bumblebee202111.minusone.server.entity.Account
import com.github.bumblebee202111.minusone.server.entity.Profile

data class PhoneAccountInfoDto(
    val exist: Int, 
    val nickname: String?,
    val hasPassword: Boolean,
    val hasSnsBinded: Boolean?, 
    val avatarUrl: String?,
    val countryCode: String?,
    val cellphone: String
) {
    companion object {

        private fun maskPhone(phone: String): String {
            return if (phone.length > 7) {
                phone.replaceRange(3, 7, "****")
            } else {
                phone
            }
        }

        fun found(account: Account, profile: Profile): PhoneAccountInfoDto {
            return PhoneAccountInfoDto(
                exist = 1,
                nickname = profile.nickname,
                hasPassword = true,
                hasSnsBinded = false, 
                avatarUrl = profile.avatarUrl,
                countryCode = "86", 
                cellphone = maskPhone(account.phone)
            )
        }

        fun notFound(phone: String, countryCode: String?): PhoneAccountInfoDto {
            return PhoneAccountInfoDto(
                exist = -1,
                nickname = null,
                hasPassword = false,
                hasSnsBinded = null,
                avatarUrl = null,
                countryCode = countryCode,
                cellphone = maskPhone(phone)
            )
        }
    }
}
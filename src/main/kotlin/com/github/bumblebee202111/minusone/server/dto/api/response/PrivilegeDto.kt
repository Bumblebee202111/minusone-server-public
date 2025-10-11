package com.github.bumblebee202111.minusone.server.dto.api.response

data class PrivilegeDto(
    val id: Long,
    val fee: Int = 0,
    val pl: Int = 320000,
    val dl: Int = 320000
) {
    companion object {
        fun createPlaceholder(id: Long): PrivilegeDto =
            PrivilegeDto(
                id = id
            )
    }
}
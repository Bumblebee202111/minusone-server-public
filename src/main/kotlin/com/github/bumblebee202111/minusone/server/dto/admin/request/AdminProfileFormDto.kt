package com.github.bumblebee202111.minusone.server.dto.admin.request

import org.hibernate.validator.constraints.URL

data class AdminProfileFormDto(
    @URL
    val avatarUrl:String?,
    @URL
    val backgroundUrl:String?
)
package com.github.bumblebee202111.minusone.server.dto.api.internal

data class RegisterRequest(
    val phone: String,
    val password: String,
    val nickname: String,
    val captcha: String?=null,
    val countrycode: String? = "86"
)
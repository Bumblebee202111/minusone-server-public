package com.github.bumblebee202111.minusone.server.dto.api.internal

import jakarta.validation.constraints.NotBlank

data class CellphoneLoginRequest(
    @NotBlank
    val phone:String,
    @NotBlank
    val password:String,
    val countrycode:String?=null
)

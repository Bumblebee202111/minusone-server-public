package com.github.bumblebee202111.minusone.server.exception.api

open class ApiException(
    val code: Int,
    override val message: String
) : RuntimeException(message)



package com.github.bumblebee202111.minusone.server.resolver

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class JsonQueryParam(
    val value: String = ""
)
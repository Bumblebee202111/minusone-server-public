package com.github.bumblebee202111.minusone.server.advice

import com.github.bumblebee202111.minusone.server.dto.api.response.ApiResponse
import com.github.bumblebee202111.minusone.server.exception.api.WrrongParamException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice(basePackages = ["com.github.bumblebee202111.minusthree.controller.api"])
class GlobalExceptionHandler {
    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ApiResponse {
        return when (ex) {
            is WrrongParamException -> ApiResponse.error(400, "参数错误")
            else -> ApiResponse.error(500, "服务器错误")
        }
    }
}
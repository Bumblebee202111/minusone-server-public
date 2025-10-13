package com.github.bumblebee202111.minusone.server.advice.api

import com.github.bumblebee202111.minusone.server.constant.api.ApiCodes
import com.github.bumblebee202111.minusone.server.constant.api.ApiErrorMessages
import com.github.bumblebee202111.minusone.server.dto.api.response.ApiResponse
import com.github.bumblebee202111.minusone.server.exception.api.ApiException
import com.github.bumblebee202111.minusone.server.exception.api.LogoutException
import com.github.bumblebee202111.minusone.server.exception.api.WrrongParamException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice(basePackages = ["com.github.bumblebee202111.minusone.server.controller.api"])
class ApiGlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(ApiGlobalExceptionHandler::class.java)

    @ExceptionHandler(ApiException::class)
    fun handleApiException(ex: ApiException): ResponseEntity<ApiResponse> {
        when (ex) {
            is LogoutException -> log.info("Logout: code={}, message='{}'", ex.code, ex.message)
            is WrrongParamException -> log.warn("WrrongParam: code={}, message='{}'", ex.code, ex.message)
            else -> log.warn("ApiException: code={}, message='{}'", ex.code, ex.message)
        }
        return ResponseEntity.ok(ApiResponse.error(ex.code, ex.message))
    }

    @ExceptionHandler(
        MissingServletRequestParameterException::class,
        MethodArgumentTypeMismatchException::class,
        HttpMessageNotReadableException::class,
        MethodArgumentNotValidException::class
    )
    fun handleClientInputException(ex: Exception): ResponseEntity<ApiResponse> {
        when (ex) {
            is MissingServletRequestParameterException -> log.warn("Missing param: '{}'", ex.parameterName)
            is MethodArgumentTypeMismatchException -> log.warn("Type mismatch for param: '{}'", ex.name)
            is HttpMessageNotReadableException -> log.warn("Malformed request body: {}", ex.mostSpecificCause.message ?: ex.message)
            is MethodArgumentNotValidException -> {
                val errors = ex.bindingResult.fieldErrors.joinToString(", ") { "'${it.field}': ${it.defaultMessage}" }
                log.warn("DTO validation failed: {}", errors)
            }
            else -> log.warn("Unhandled client input exception: {}", ex.message)
        }

        return ResponseEntity.ok(
            ApiResponse.error(
                ApiCodes.CODE_WRRONG_PARAM,
                ApiErrorMessages.MSG_WRRONG_PARAM
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiResponse> {
        log.error("Unhandled exception: {}", ex.message, ex)
        return ResponseEntity.ok(
            ApiResponse.error(
                ApiCodes.CODE_SERVER_ERROR,
                ApiErrorMessages.MSG_SERVER_ERROR
            )
        )
    }
}
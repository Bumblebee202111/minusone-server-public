package com.github.bumblebee202111.minusone.server.exception.admin.handler

import com.github.bumblebee202111.minusone.server.dto.admin.response.AdminErrorResponse
import com.github.bumblebee202111.minusone.server.exception.admin.DataConflictException
import com.github.bumblebee202111.minusone.server.exception.admin.ResourceNotFoundException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.github.bumblebee202111.minusone.server.controller.admin"])
class AdminGlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(AdminGlobalExceptionHandler::class.java)

    @ExceptionHandler(DataConflictException::class)
    fun handleDataConflict(ex: DataConflictException, request: HttpServletRequest): ResponseEntity<AdminErrorResponse> {
        log.warn("Admin Data Conflict: ${ex.message} for path ${request.requestURI}")
        val errorResponse = AdminErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = HttpStatus.CONFLICT.reasonPhrase,
            message = ex.message,
            path = request.requestURI
        )
        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(ex: ResourceNotFoundException, request: HttpServletRequest): ResponseEntity<AdminErrorResponse> {
        log.warn("Admin Resource Not Found: {} for path {}", ex.message, request.requestURI)

        val errorResponse = AdminErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.reasonPhrase,
            message = ex.message,
            path = request.requestURI
        )
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception, request: HttpServletRequest): ResponseEntity<AdminErrorResponse> {
        log.error("Unhandled Admin Exception for path ${request.requestURI}", ex)

        val errorResponse = AdminErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Internal Server Error",
            message = "An unexpected error occurred. Please contact support.",
            path = request.requestURI
        )
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
package com.github.bumblebee202111.minusone.server.dto.admin.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AdminErrorResponse(
    val timestamp: Long = System.currentTimeMillis(),
    val status: Int,
    val error: String,
    val message: String?,
    val path: String?,
    val details: List<String>? = null
) {
    companion object {
         fun badRequest(message: String, path: String?, details: List<String>? = null): AdminErrorResponse {
             return AdminErrorResponse(
                 status = 400,
                 error = "Bad Request",
                 message = message,
                 path = path,
                 details = details
             )
         }
     }
}
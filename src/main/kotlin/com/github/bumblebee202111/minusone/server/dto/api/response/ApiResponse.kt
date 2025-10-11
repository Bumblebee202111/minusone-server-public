package com.github.bumblebee202111.minusone.server.dto.api.response

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue



@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse(
    val code: Int,
    var message: String? = null,
    var msg: String? = null,
    var data: Any? = null,

    @JsonIgnore
    private val _additionalProperties: MutableMap<String, Any?> = mutableMapOf()
) {

    @JsonAnyGetter
    fun getAdditionalProperties(): Map<String, Any?> = _additionalProperties

    fun add(key: String, value: Any?): ApiResponse {
        _additionalProperties[key] = value
        return this
    }

    fun addAll(properties: Map<String, Any?>): ApiResponse {
        _additionalProperties.putAll(properties)
        return this
    }

    companion object {
        @JvmOverloads
        fun success(data: Any? = null, code:Int = 200): ApiResponse {
            return ApiResponse(code = code, data = data)
        }

        fun error(code: Int, message: String): ApiResponse {
            return ApiResponse(code = code, message = message, msg = message)
        }
        inline fun <reified T : Any> successFlattened(
            dataToFlatten: T,
            objectMapper: ObjectMapper,
            code: Int = 200
        ): ApiResponse {
            val response = ApiResponse(code = code)

            val propertiesMap: Map<String, Any?> = objectMapper.convertValue(dataToFlatten)

            response.addAll(propertiesMap)

            return response
        }
    }
}
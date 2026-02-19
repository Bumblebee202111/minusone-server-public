package com.github.bumblebee202111.minusone.server.advice.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPOutputStream

@RestControllerAdvice(basePackages = ["com.github.bumblebee202111.minusone.server.controller.api"])
class EapiGzipResponseAdvice(
    private val objectMapper: ObjectMapper
) : ResponseBodyAdvice<Any> {

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>
    ): Boolean {
        return true
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        val servletRequest = (request as? ServletServerHttpRequest)?.servletRequest

        if (body == null || servletRequest == null) {
            return body
        }

        if (servletRequest.requestURI.startsWith("/eapi") && "true" == servletRequest.getHeader("x-aeapi")) {
            val jsonString = objectMapper.writeValueAsString(body)
            
            val compressedBytes = ByteArrayOutputStream().use { byteArrayOutputStream ->
                GZIPOutputStream(byteArrayOutputStream).use { gzipOutputStream ->
                    gzipOutputStream.write(jsonString.toByteArray(Charsets.UTF_8))
                }
                byteArrayOutputStream.toByteArray()
            }

            response.headers.contentType = MediaType.APPLICATION_JSON
            
            response.body.write(compressedBytes)
            
            return null
        }

        return body
    }
}
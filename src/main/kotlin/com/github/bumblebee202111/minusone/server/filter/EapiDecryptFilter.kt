package com.github.bumblebee202111.minusone.server.filter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.bumblebee202111.minusone.server.dto.api.response.ApiResponse
import com.github.bumblebee202111.minusone.server.util.CryptoUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*

@Component
@Order(1)
class EapiDecryptFilter(
    private val objectMapper: ObjectMapper,
) : OncePerRequestFilter() {

    companion object {
        private val log = LoggerFactory.getLogger(EapiDecryptFilter::class.java)
        private const val EAPI_AES_KEY = "****************"
        private const val MAGIC_SEPARATOR = "*************"
        private const val API_PATH_PREFIX = "/api"
        private const val EAPI_PATH_PREFIX = "/eapi"
        private const val MD5_DIGEST_PREFIX = "******"
        private const val MD5_DIGEST_INFIX = "***"
        private const val MD5_DIGEST_SUFFIX = "*************"
        private const val E_R_PARAM = "e_r"
        private const val DEFAULT_ERROR_MESSAGE = "参数错误"
        private const val DEFAULT_ERROR_CODE = 400

    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        if (!request.requestURI.startsWith(EAPI_PATH_PREFIX)) {
            chain.doFilter(request, response)
            return
        }
        val encryptedBase64Params = request.getParameter("params")
        if (encryptedBase64Params.isNullOrBlank()) {
            log.warn("EAPI request to ${request.requestURI} without 'params' field.")
            sendErrorResponse(response)
            return
        }
        try {
            val decryptedData = CryptoUtil.aesEcbDecryptFromBase64(encryptedBase64Params, EAPI_AES_KEY)
            val (decryptedApiPath, paramsJson, clientDigest) = parseDecryptedData(decryptedData)

            validateRequestPath(request, decryptedApiPath)
            validateSignature(decryptedApiPath, paramsJson, clientDigest)

            val paramsMap = parseAndProcessParams(paramsJson)
            val encryptResponse = paramsMap[E_R_PARAM] as? Boolean ?: false

            request.setAttribute("EAPI_ENCRYPT_RESPONSE", encryptResponse)

            val targetApiPath = decryptedApiPath
            val queryParamsString = paramsMap
                .filterKeys { it != E_R_PARAM }
                .entries
                .joinToString("&") { (key, value) ->
                    val valueString = value as? String ?: objectMapper.writeValueAsString(value)
                    "${encodeURL(key)}=${encodeURL(valueString)}"
                }

            val wrappedRequest = createForwardableRequest(request, targetApiPath, queryParamsString)
            log.debug("Forwarding EAPI request from ${request.requestURI} to GET $targetApiPath?$queryParamsString")
            request.getRequestDispatcher(targetApiPath).forward(wrappedRequest, response)
        } catch (e: EapiValidationException) {
            log.warn("EAPI validation failed for ${request.requestURI}: ${e.message}")
            sendErrorResponse(response)
        } catch (e: Exception) {
            log.error("Unexpected error during EAPI processing for ${request.requestURI}: ${e.message}", e)
            sendErrorResponse(response, "EAPI processing failed internally")
        }
    }

    private fun parseDecryptedData(decryptedData: String): Triple<String, String, String> {
        val parts = decryptedData.split(MAGIC_SEPARATOR)
        if (parts.size != 3) {
            throw EapiValidationException("Invalid EAPI decrypted data format (parts count: ${parts.size})")
        }
        return try {
            Triple(URLDecoder.decode(parts[0], StandardCharsets.UTF_8.name()), parts[1], parts[2])
        } catch (e: Exception) {
            throw EapiValidationException("Failed to URL decode decrypted path", e)
        }
    }

    private fun validateRequestPath(request: HttpServletRequest, decryptedApiPath: String) {
        val actualEapiPathSuffix = request.requestURI.substringAfter(EAPI_PATH_PREFIX, "")
        val expectedApiPathSuffix = decryptedApiPath.substringAfter(API_PATH_PREFIX, "")

        if (actualEapiPathSuffix != expectedApiPathSuffix) {
            throw EapiValidationException(
                "EAPI path mismatch: Request URI suffix '${actualEapiPathSuffix}' " +
                        "differs from decrypted API path suffix '${expectedApiPathSuffix}'."
            )
        }
    }

    private fun validateSignature(decryptedApiPath: String, paramsJson: String, clientDigest: String) {
        val md5Input = "$MD5_DIGEST_PREFIX${decryptedApiPath}$MD5_DIGEST_INFIX$paramsJson$MD5_DIGEST_SUFFIX"
        val serverDigest = CryptoUtil.md5(md5Input)
        if (serverDigest != clientDigest) {
            throw EapiValidationException("Signature mismatch. Client: $clientDigest, Server: $serverDigest. Input: $md5Input")
        }
    }

    private fun parseAndProcessParams(paramsJson: String): Map<String, Any> {
        try {
            val params = objectMapper.readValue(paramsJson, object : TypeReference<Map<String, Any>>() {})
                .toMutableMap()

            params[E_R_PARAM]?.let { eRValue ->
                params[E_R_PARAM] = when (eRValue) {
                    is Boolean -> eRValue
                    "true" -> true
                    "false" -> false
                    else -> throw EapiValidationException("Invalid value for 'e_r' parameter: $eRValue")
                }
            }
            return params
        } catch (e: EapiValidationException) {
            throw e
        } catch (e: Exception) {
            throw EapiValidationException("Failed to parse EAPI parameters JSON: ${e.message}", e)
        }
    }

    private fun createForwardableRequest(
        originalRequest: HttpServletRequest,
        targetApiPath: String,
        queryParams: String
    ): HttpServletRequest {
        return object : HttpServletRequestWrapper(originalRequest) {
            override fun getMethod(): String = "GET"
            override fun getRequestURI() = targetApiPath
            override fun getQueryString() = queryParams.takeIf { it.isNotEmpty() }

            private val parsedQueryParams: Map<String, Array<String>> by lazy {
                if (queryParams.isBlank()) {
                    emptyMap()
                } else {
                    queryParams.split('&')
                        .mapNotNull { param ->
                            val parts = param.split("=", limit = 2)
                            if (parts.size == 2) {
                                URLDecoder.decode(parts[0], StandardCharsets.UTF_8.name()) to
                                        arrayOf(URLDecoder.decode(parts[1], StandardCharsets.UTF_8.name()))
                            } else { null }
                        }
                        .groupBy({ it.first }, { it.second.first() })
                        .mapValues { it.value.toTypedArray() }
                }
            }
            override fun getParameter(name: String): String? = parsedQueryParams[name]?.firstOrNull()
            override fun getParameterValues(name: String): Array<String>? = parsedQueryParams[name]
            override fun getParameterMap(): Map<String, Array<String>> = parsedQueryParams
            override fun getParameterNames(): Enumeration<String> = Collections.enumeration(parsedQueryParams.keys)
        }
    }

    private fun sendErrorResponse(response: HttpServletResponse, logHint: String? = null)  {
        if (logHint != null) {
            log.warn("Sending default EAPI error. Server-side hint: $logHint")
        }
        try {
            val apiResponse = ApiResponse.error(DEFAULT_ERROR_CODE, DEFAULT_ERROR_MESSAGE)
            response.status = HttpServletResponse.SC_OK
            response.contentType = "application/json;charset=UTF-8"
            response.writer.write(objectMapper.writeValueAsString(apiResponse))
        } catch (e: IOException) {
            log.error("Failed to write default error response: ${e.message}", e)
        }
    }

    private fun encodeURL(str: String) = URLEncoder.encode(str, "UTF-8")

    private class EapiValidationException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
}
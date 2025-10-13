package com.github.bumblebee202111.minusone.server.filter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.bumblebee202111.minusone.server.dto.api.response.ApiResponse
import com.github.bumblebee202111.minusone.server.exception.api.WrrongParamException
import com.github.bumblebee202111.minusone.server.util.CryptoUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.zip.GZIPOutputStream

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

        try {
            val encryptedParams = request.getParameter("params")
                ?: throw WrrongParamException() 

            val decryptedData = CryptoUtil.aesEcbDecryptFromBase64(encryptedParams, EAPI_AES_KEY)

            val parts = decryptedData.split(MAGIC_SEPARATOR)
            if (parts.size != 3) throw WrrongParamException()

            val (decryptedApiPath, paramsJson, clientDigest) = parts.let {
                Triple(URLDecoder.decode(it[0], StandardCharsets.UTF_8.name()), it[1], it[2])
            }

            validateRequestPath(request, decryptedApiPath)
            validateSignature(decryptedApiPath, paramsJson, clientDigest)

            val paramsMap = parseAndProcessParams(paramsJson)
            request.setAttribute("EAPI_ENCRYPT_RESPONSE", paramsMap[E_R_PARAM] as? Boolean ?: false)

            val queryParamsString = paramsMap
                .filterKeys { it != E_R_PARAM }
                .entries
                .joinToString("&") { (key, value) ->
                    val valueString = value as? String ?: objectMapper.writeValueAsString(value)
                    "${encodeURL(key)}=${encodeURL(valueString)}"
                }

            val wrappedRequest = createForwardableRequest(request, decryptedApiPath, queryParamsString)
            request.getRequestDispatcher(decryptedApiPath).forward(wrappedRequest, response)

        } catch (e: Exception) {
            log.warn("EAPI processing failed for [{}]: {}", request.requestURI, e.message)
            sendEapiErrorResponse(request, response)
        }
    }

    private fun sendEapiErrorResponse(request: HttpServletRequest, response: HttpServletResponse) {
        try {
            val exception = WrrongParamException()
            val apiResponse = ApiResponse.error(exception.code, exception.message)
            val jsonString = objectMapper.writeValueAsString(apiResponse)
            val responseBytes = jsonString.toByteArray(Charsets.UTF_8)

            if ("true" == request.getHeader("x-aeapi")) {
                val byteArrayOutputStream = ByteArrayOutputStream()
                GZIPOutputStream(byteArrayOutputStream).use { it.write(responseBytes) }
                val gzippedBytes = byteArrayOutputStream.toByteArray()

                response.status = HttpServletResponse.SC_OK
                response.contentType = "application/json;charset=UTF-8"
                response.setContentLength(gzippedBytes.size)
                response.outputStream.write(gzippedBytes)
            } else {
                response.status = HttpServletResponse.SC_OK
                response.contentType = "application/json;charset=UTF-8"
                response.setContentLength(responseBytes.size)
                response.writer.write(jsonString)
            }
        } catch (e: IOException) {
            log.error("Failed to write EAPI error response", e)
        }
    }

    private fun validateRequestPath(request: HttpServletRequest, decryptedApiPath: String) {
        val actualSuffix = request.requestURI.substringAfter(EAPI_PATH_PREFIX, "")
        val expectedSuffix = decryptedApiPath.substringAfter(API_PATH_PREFIX, "")
        if (actualSuffix != expectedSuffix) {
            throw WrrongParamException()
        }
    }


    private fun validateSignature(decryptedApiPath: String, paramsJson: String, clientDigest: String) {
        val md5Input = "$MD5_DIGEST_PREFIX${decryptedApiPath}$MD5_DIGEST_INFIX$paramsJson$MD5_DIGEST_SUFFIX"
        val serverDigest = CryptoUtil.md5(md5Input)
        if (serverDigest != clientDigest) {
            throw WrrongParamException()
        }
    }

    private fun parseAndProcessParams(paramsJson: String): Map<String, Any> {
        val params = objectMapper.readValue(paramsJson, object : TypeReference<Map<String, Any>>() {})
            .toMutableMap()
        params[E_R_PARAM]?.let {
            params[E_R_PARAM] = when (it) {
                is Boolean -> it
                "true" -> true
                "false" -> false
                else -> throw WrrongParamException()
            }
        }
        return params
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
                            } else {
                                null
                            }
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

    private fun encodeURL(str: String) = URLEncoder.encode(str, "UTF-8")

}
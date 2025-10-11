package com.github.bumblebee202111.minusone.server.resolver

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class JsonQueryParamArgumentResolver(private val objectMapper: ObjectMapper) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(JsonQueryParam::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val annotation = parameter.getParameterAnnotation(JsonQueryParam::class.java)!!

        val paramName = annotation.value.ifBlank { parameter.parameterName!! }

        return webRequest.getParameter(paramName)?.let {
            objectMapper.readValue(it, parameter.parameterType)
        }
    }
}
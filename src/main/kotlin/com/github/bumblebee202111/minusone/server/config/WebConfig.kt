package com.github.bumblebee202111.minusone.server.config

import com.github.bumblebee202111.minusone.server.resolver.JsonQueryParamArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(private val jsonQueryParamArgumentResolver: JsonQueryParamArgumentResolver):WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(jsonQueryParamArgumentResolver)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .addResourceHandler("/songs/**")
            .addResourceLocations("classpath:/static/songs/")
    }
}
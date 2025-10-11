package com.github.bumblebee202111.minusone.server.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig:WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/admin/**")
            .allowedOrigins("http:
            .allowedMethods("GET", "POST")
    }
}
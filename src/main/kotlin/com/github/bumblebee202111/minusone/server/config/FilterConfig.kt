package com.github.bumblebee202111.minusone.server.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.bumblebee202111.minusone.server.security.filter.EapiDecryptFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered

@Configuration
class FilterConfig {

    @Configuration
    class FilterConfig {
        @Bean
        fun eapiFilterRegistration(objectMapper: ObjectMapper): FilterRegistrationBean<EapiDecryptFilter> {
            val filter = EapiDecryptFilter(objectMapper)
            return FilterRegistrationBean(filter).apply {
                addUrlPatterns("/eapi/*")
                order = Ordered.HIGHEST_PRECEDENCE
            }
        }
    }
}
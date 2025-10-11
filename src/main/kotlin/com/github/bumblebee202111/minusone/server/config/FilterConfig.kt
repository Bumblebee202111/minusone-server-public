package com.github.bumblebee202111.minusone.server.config

import com.github.bumblebee202111.minusone.server.filter.EapiDecryptFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered

@Configuration
class FilterConfig {

    @Bean
    fun eapiFilterRegistration(eapiDecryptFilter: EapiDecryptFilter): FilterRegistrationBean<*> {
        return FilterRegistrationBean<EapiDecryptFilter>().apply {
            filter = eapiDecryptFilter
            addUrlPatterns("/eapi/*")
            order = Ordered.HIGHEST_PRECEDENCE
        }
    }
}
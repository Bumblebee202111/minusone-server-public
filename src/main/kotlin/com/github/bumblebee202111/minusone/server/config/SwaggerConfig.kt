package com.github.bumblebee202111.minusone.server.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("MinusOne Server API")
                    .description("API documentation for MinusOne Server")
                    .version("v0.0.1")
            )
    }
}

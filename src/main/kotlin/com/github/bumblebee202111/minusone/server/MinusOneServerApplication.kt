package com.github.bumblebee202111.minusone.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc


@SpringBootApplication(exclude = [ SecurityAutoConfiguration::class, SecurityFilterAutoConfiguration::class])
@EnableWebMvc
class MinusOneServerApplication

fun main(args: Array<String>) {
    runApplication<MinusOneServerApplication>(*args)
}

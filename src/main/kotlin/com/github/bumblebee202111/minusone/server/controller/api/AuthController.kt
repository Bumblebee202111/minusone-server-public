package com.github.bumblebee202111.minusone.server.controller.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.bumblebee202111.minusone.server.constant.api.ApiCodes
import com.github.bumblebee202111.minusone.server.constant.api.ApiConstants
import com.github.bumblebee202111.minusone.server.service.api.AuthService
import com.github.bumblebee202111.minusone.server.service.api.ProfileService
import com.github.bumblebee202111.minusone.server.dto.api.response.ApiResponse
import com.github.bumblebee202111.minusone.server.dto.api.internal.CellphoneLoginRequest
import com.github.bumblebee202111.minusone.server.dto.api.internal.RegisterAnonimousResult
import com.github.bumblebee202111.minusone.server.dto.api.internal.RegisterRequest
import com.github.bumblebee202111.minusone.server.dto.api.response.toDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.constraints.NotBlank
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/eapi")
@Tag(name = "Auth", description = "Authentication APIs")
class AuthController(
    private val authService: AuthService,
    private val profileService: ProfileService,
    private val objectMapper: ObjectMapper
) {

    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @RequestMapping("/register/anonimous")
    @Operation(summary = "Register anonymously", description = "Registers a new user anonymously")
    fun registerAnonimous(
        @RequestParam username: String
    ): RegisterAnonimousResult {
        return authService.registerAnonimously(username)
    }

    @RequestMapping("/register/cellphone")
    @Operation(summary = "Register with cellphone", description = "Registers a new user with cellphone")
    fun registerWithCellphone(
        @NotBlank
        @RequestParam phone: String,
        @RequestParam(defaultValue = "86") countrycode: String,
        @NotBlank
        @RequestParam captcha: String,
        @RequestParam nickname: String,
        @NotBlank
        @RequestParam password: String,
        response: HttpServletResponse
    ): ApiResponse {
        val registerRequest = RegisterRequest(
            phone = phone,
            password = password,
            captcha = captcha,
            nickname = nickname
        )

        val (account, musicUToken) = authService.registerByCellphone(
            registerRequest
        )
        val cookie = Cookie(ApiConstants.COOKIE_MUSIC_U, musicUToken).apply {
            path = "/"
            maxAge = 30 * 24 * 60 * 60
            isHttpOnly = true
        }
        response.addCookie(cookie)
        return ApiResponse.success()
    }

    @RequestMapping("/login/cellphone")
    @Operation(summary = "Login with cellphone", description = "Logs in a user with cellphone")
    fun cellphoneLogin(
        @RequestParam phone: String,
        @RequestParam(required = false, defaultValue = "86") countrycode: String?,
        @RequestParam(required = false) password: String,
        response: HttpServletResponse
    ): ApiResponse {
        log.info(
            "Attempting cellphone login for phone: {}, countrycode: {}",
            phone, countrycode
        )
        val loginRequest = CellphoneLoginRequest(
            phone = phone,
            password = password,
            countrycode = countrycode
        )
        val (account, musicUToken) = authService.cellphoneLogin(loginRequest)
        val profile = profileService.findByAccountId(account.id)
        if (profile == null) {
            log.error(
                "User {} (ID: {}) logged in successfully but profile not found via service!",
                account.userName,
                account.id
            )
            return ApiResponse.error(ApiCodes.CODE_SERVER_ERROR, "用户数据不完整，请稍后再试 (profile missing)")
        }
        val cookie = Cookie(ApiConstants.COOKIE_MUSIC_U, musicUToken).apply {
            path = "/"
            maxAge = 30 * 24 * 60 * 60
            isHttpOnly = true
        }
        response.addCookie(cookie)

        val accountDto = account.toDto()
        val profileDto = profile.toDto()

        response.addCookie(cookie)
        return ApiResponse.success().apply {
            add("loginType", 1)
            add("account", accountDto)
            add("token", musicUToken)
            add("profile", profileDto)
        }
    }

    @RequestMapping("/logout")
    @Operation(summary = "Logout", description = "Logs out the current user")
    fun logout(response: HttpServletResponse): ApiResponse {
        authService.logout(response)
        return ApiResponse.success()
    }

    @RequestMapping("cellphone/existence/check")
    @Operation(summary = "Check cellphone existence", description = "Checks if a cellphone is already registered")
    fun checkCellphoneExistence(
        @RequestParam @NotBlank cellphone: String,
        @RequestParam(defaultValue = "86") countrycode: String
    ): ApiResponse {
        val resultDto = authService.checkCellphoneExistence(cellphone, countrycode)

        return ApiResponse.successFlattened(resultDto, objectMapper)
    }

}

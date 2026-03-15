package com.github.bumblebee202111.minusone.server.controller.admin

import com.github.bumblebee202111.minusone.server.dto.admin.request.AdminLoginRequest
import com.github.bumblebee202111.minusone.server.dto.admin.response.AdminLoginResponse
import com.github.bumblebee202111.minusone.server.util.JwtUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/auth")
@Tag(name = "Admin Auth", description = "Admin Authentication APIs")
class AdminAuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/login")
    @Operation(summary = "Admin Login", description = "Authenticates admin and returns a JWT token")
    fun login(@RequestBody request: AdminLoginRequest): AdminLoginResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        
        val token = jwtUtil.generateToken(request.username)
        return AdminLoginResponse(token)
    }
}
package com.skytree.skystock.controller

import com.skytree.skystock.dto.LoginRequest
import com.skytree.skystock.dto.RegisterRequest
import com.skytree.skystock.dto.TokenResponse
import com.skytree.skystock.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody req: RegisterRequest): TokenResponse =
        authService.register(req)

    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): TokenResponse =
        authService.login(req)
}

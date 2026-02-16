package com.skytree.skystock.dto

import com.skytree.skystock.entity.UserRole

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val role: UserRole = UserRole.VIEWER
)

data class TokenResponse(
    val accessToken: String,
    val userId: Int,
    val username: String,
    val role: UserRole
)

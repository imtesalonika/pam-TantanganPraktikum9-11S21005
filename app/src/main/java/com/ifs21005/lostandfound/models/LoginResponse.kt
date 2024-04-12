package com.ifs21005.lostandfound.models

data class LoginResponse(
    val `data`: Data,
    val message: String,
    val success: Boolean
)
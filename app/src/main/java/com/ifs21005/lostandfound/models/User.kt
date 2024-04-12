package com.ifs21005.lostandfound.models

data class User(
    val created_at: String,
    val email: String,
    val email_verified_at: Any,
    val id: Int,
    val name: String,
    val photo: String,
    val updated_at: String
)
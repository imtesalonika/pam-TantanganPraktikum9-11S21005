package com.ifs21005.lostandfound.models

data class RegisterRequest(
    val name :String,
    val email : String,
    val password : String
)

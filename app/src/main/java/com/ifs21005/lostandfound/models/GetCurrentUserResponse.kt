package com.ifs21005.lostandfound.models

data class GetCurrentUserResponse(
    val `data`: DataGetCurrentUser,
    val message: String,
    val success: Boolean
)
package com.ifs21005.lostandfound.models

data class AddLostFoundResponse(
    val `data`: DataAddLostFoundResponse,
    val message: String,
    val success: Boolean
)
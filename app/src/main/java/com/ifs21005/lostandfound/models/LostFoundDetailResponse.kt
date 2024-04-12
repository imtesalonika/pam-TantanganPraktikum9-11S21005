package com.ifs21005.lostandfound.models

data class LostFoundDetailResponse(
    val `data`: DataX,
    val message: String,
    val success: Boolean
)
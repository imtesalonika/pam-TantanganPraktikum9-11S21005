package com.ifs21005.lostandfound.models

data class GetAllLostAndFoundsResponse(
    val `data`: ListLostFounds,
    val message: String,
    val success: Boolean
)
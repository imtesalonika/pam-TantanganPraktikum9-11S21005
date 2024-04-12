package com.ifs21005.lostandfound.models

data class LostFoundX(
    val author: AuthorX,
    val cover: Any,
    val created_at: String,
    val description: String,
    val id: Int,
    val is_completed: Int,
    val status: String,
    val title: String,
    val updated_at: String,
    val user_id: Int
)
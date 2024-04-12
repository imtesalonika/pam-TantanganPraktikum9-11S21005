package com.ifs21005.lostandfound.models

import com.google.gson.annotations.SerializedName

data class LostFoundXX(
    val author: Author,
    val cover: String,
    @SerializedName("created_at") val createdAt: String,
    val description: String,
    val id: Int,
    @SerializedName("is_completed") val isCompleted: Int,
    val status: String,
    val title: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("user_id") val userId: Int
)
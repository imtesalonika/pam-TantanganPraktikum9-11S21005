package com.ifs21005.lostandfound.models

import com.google.gson.annotations.SerializedName

data class UpdateData(
    val title : String,
    val description : String,
    val status : String,
    @SerializedName("is_completed")
    val isCompleted : Int
)

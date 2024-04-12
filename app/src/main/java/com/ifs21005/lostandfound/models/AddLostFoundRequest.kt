package com.ifs21005.lostandfound.models

data class AddLostFoundRequest(
    val title: String,
    val description: String,
    val status: String
)

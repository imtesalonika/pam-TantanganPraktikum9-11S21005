package com.ifs21005.lostandfound.models

import com.google.gson.annotations.SerializedName

data class ListLostFounds(
    @SerializedName("lost_founds") val lostFounds: ArrayList<LostFoundXX>
)
package com.ifs21005.lostandfound

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "lostfound_ditandai")
class LostFoundEntity (
    @PrimaryKey
    val id : Int,

    @ColumnInfo("title")
    val title : String,

    @ColumnInfo("description")
    val description : String,

    @ColumnInfo("user_name")
    val username : String
)



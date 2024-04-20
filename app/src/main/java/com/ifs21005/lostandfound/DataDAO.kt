package com.ifs21005.lostandfound

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DataDAO {
    @Insert
    suspend fun savelostfound(lostFoundEntity: LostFoundEntity)

    @Query(
        "SELECT * FROM lostfound_ditandai"
    )
    fun getAllSavedLostFound ():LiveData<List<LostFoundEntity>>

    @Query(
        "DELETE FROM lostfound_ditandai where id=:id"
    )
    fun removeSaved (id:Int)

    @Delete
    suspend fun deleteSaved(lostFoundEntity: LostFoundEntity)
}
package com.ifs21005.lostandfound

class LostFoundRepository (private val dataDAO: DataDAO){
    suspend fun saveLostFound(lostFoundEntity: LostFoundEntity){
        dataDAO.savelostfound(lostFoundEntity)
    }
    suspend fun deleteLostFound(id:Int){
       dataDAO.removeSaved(id)
    }
    val lostFounds= dataDAO.getAllSavedLostFound()


}
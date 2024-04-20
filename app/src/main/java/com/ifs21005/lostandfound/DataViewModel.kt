package com.ifs21005.lostandfound

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataViewModel(application: Application):AndroidViewModel(application){
    val getAllSaved :LiveData<List<LostFoundEntity>>
    val repo:LostFoundRepository

    init {
        val dao=MyDatabase.createDatabase(application).getDataDAO()
        repo = LostFoundRepository(dao)
        getAllSaved=repo.lostFounds
    }

    fun save(data:LostFoundEntity){
        viewModelScope.launch (Dispatchers.IO){repo.saveLostFound(data)}
    }

    fun delete(id:Int){
        viewModelScope.launch (Dispatchers.IO){repo.deleteLostFound(id)}
    }

}

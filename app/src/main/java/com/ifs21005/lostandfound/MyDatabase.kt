package com.ifs21005.lostandfound

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LostFoundEntity::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase(){
    abstract fun getDataDAO():DataDAO

    companion object{
        @Volatile
        private var database:MyDatabase? = null

        fun createDatabase(context:Context):MyDatabase{
            val databaseSementara= database

            if (databaseSementara!=null){
                return databaseSementara
            }

            synchronized(this){
                val newDatabase= Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "my_database"
                ).build()
                database=newDatabase
                return database as MyDatabase
            }
        }

    }
}
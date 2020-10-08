package com.zgw.company.base.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zgw.company.base.core.MyApplication
import com.zgw.company.base.core.config.Config

@Database(entities = [FavoriteEntity::class,NullEntity::class],version = 1)
abstract class DBHelper : RoomDatabase(){
    abstract fun getSourceDao() : SourceDao
    abstract fun getNull() : NullDao

    companion object{
        private var db : DBHelper? = null

        fun getInstance(context: Context = MyApplication.instance) : DBHelper{
            if (db == null){
                db = Room.databaseBuilder(context,DBHelper::class.java, Config.DB_NAME).build()
            }
            return db!!
        }
    }
}
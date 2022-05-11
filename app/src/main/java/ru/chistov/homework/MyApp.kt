package ru.chistov.homework

import android.app.Application
import androidx.room.Room
import ru.chistov.homework.domain.room.HistoryDao
import ru.chistov.homework.domain.room.MyDB


class MyApp:Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object{
        private var db:MyDB?=null
        private var appContext:MyApp?=null
        fun getHistoryDao():HistoryDao{
            if(db==null){
                if(appContext!=null){
                    db = Room.databaseBuilder(appContext!!,MyDB::class.java,"test").build()
                }else{
                    throw IllegalStateException("что-то пошло не так")
                }
            }
            return db!!.historyDao()
        }


    }

}
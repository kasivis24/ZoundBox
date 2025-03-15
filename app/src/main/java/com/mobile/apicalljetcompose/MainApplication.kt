package com.mobile.apicalljetcompose

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.mobile.apicalljetcompose.ui.theme.database.AppDatabase

class MainApplication : Application(){
    companion object{
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        Log.d("Screen","initapp")
        database = Room.databaseBuilder(applicationContext,AppDatabase::class.java,AppDatabase.DATA_NAME).build()
    }
}
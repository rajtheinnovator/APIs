package com.enpassio.apis.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.enpassio.apis.ListOfMailIds
import com.enpassio.apis.Message

@Database(entities = arrayOf(ListOfMailIds::class, Message::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun messageDao(): ListOfMailIdsDao

    companion object {

        private val LOG_TAG = AppDatabase::class.java.simpleName
        private val LOCK = Any()
        private val DATABASE_NAME = "messagedatabase"
        private var sInstance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase::class.java, AppDatabase.DATABASE_NAME)
                            .build()
                }
            }
            return sInstance!!
        }
    }

}
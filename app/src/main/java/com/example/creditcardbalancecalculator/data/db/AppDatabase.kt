package com.example.creditcardbalancecalculator.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.creditcardbalancecalculator.data.db.dao.MonthlyTransactionDAO
import com.example.creditcardbalancecalculator.data.db.entity.MonthlyTransaction

@Database(entities = arrayOf(MonthlyTransaction::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun monthlyTransactionDAO():MonthlyTransactionDAO
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
        fun getDatabase():AppDatabase{
            return INSTANCE?:throw Exception("Cannot get database when it's not initialized!")
        }
    }
}
package com.ktt.mylocation.user

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ktt.mylocation.LocationTable


@Database(entities = [MyTable::class, LocationTable::class], version = 2, exportSchema = false)
abstract class MyDataBase : RoomDatabase() {

    abstract fun studentDao(): MyDao

    companion object {

        @Volatile
        private var INSTANCE: MyDataBase? = null

        fun getDatabase(context: Context): MyDataBase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDataBase::class.java,
                    "student_database"
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance

                instance

            }
        }
    }
}
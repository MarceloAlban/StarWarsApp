package com.starwars.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.starwars.room.dao.FavoritePeopleDao
import com.starwars.room.entity.FavoritePeople

@Database(entities = [FavoritePeople::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritePeopleDao(): FavoritePeopleDao

    companion object {
        private var instance: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room
                        .databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "mayTheForceBeWithMeDb"
                        ).allowMainThreadQueries()
                        .build()
                }
            }

            return instance!!
        }
    }
}
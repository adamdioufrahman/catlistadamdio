package com.test.catlistdio.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.catlistdio.models.entity.UserProfileEntity

@Database(entities = [UserProfileEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    /**
     * Kembalikan contoh Dao.
     */
    abstract fun getUserProfileDao(): UserProfileDao

    companion object {
        private var DB_INSTANCE: AppDatabase? = null

        /**
         * Mengembalikan instance database aplikasi.
         */
        fun getAppDBInstance(context: Context): AppDatabase {
            if(DB_INSTANCE == null) {
                DB_INSTANCE =  Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "APP_DB")
                    .allowMainThreadQueries()
                    .build()
            }
            return DB_INSTANCE!!
        }
    }
}
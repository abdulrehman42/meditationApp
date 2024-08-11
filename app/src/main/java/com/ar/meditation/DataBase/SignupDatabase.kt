package com.ar.meditation.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ar.meditation.model.SignupModel

@Database(entities = [SignupModel::class], version = 1, exportSchema = false)
abstract class SignupDatabase : RoomDatabase() {
    abstract fun signupDao(): SignupDao

    companion object {
        @Volatile
        private var INSTANCE: SignupDatabase? = null

        fun getDatabase(context: Context): SignupDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SignupDatabase::class.java,
                    "signup_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
package com.samrudha.glb3dviewerapp.Database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Entity::class, ModelEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase(){
    abstract fun dao(): DAO
    abstract fun modelDao(): ModelDAO
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
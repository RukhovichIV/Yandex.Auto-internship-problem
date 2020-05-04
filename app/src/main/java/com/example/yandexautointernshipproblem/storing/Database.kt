package com.example.yandexautointernshipproblem.storing

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RepositoryRepresentation::class], version = 1, exportSchema = false)
abstract class RepoDatabase : RoomDatabase() {

    abstract val dao: RepoDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: RepoDatabase? = null

        fun getInstance(context: Context): RepoDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RepoDatabase::class.java,
                        "search_repo_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
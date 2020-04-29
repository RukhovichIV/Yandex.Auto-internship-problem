package com.example.yandexautointershipproblem.storing

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RepoDatabaseDao {
    @Insert
    fun insert(item: RepositoryRepresentation)

    @Update
    fun update(item: RepositoryRepresentation)

    @Query("SELECT * from search_repo_database WHERE id = :key")
    fun get(key: Int): RepositoryRepresentation

    @Query("SELECT * FROM search_repo_database ORDER BY repository_update_date DESC")
    fun getAll(): LiveData<List<RepositoryRepresentation>>

    @Query("SELECT * FROM search_repo_database WHERE repository_favor = 1 ORDER BY repository_update_date DESC")
    fun getFavourite(): LiveData<List<RepositoryRepresentation>>

    @Query("DELETE FROM search_repo_database")
    fun deleteAll()
}
package com.example.yandexautointershipproblem.storing

import androidx.room.*

@Dao
interface RepoDatabaseDao {

    @Query("select * from repository_representation_table where owner = :author and rep_title = :title")
    fun checkForRecord(author: String, title: String): RepositoryRepresentation?

    @Query("select max(id) from repository_representation_table")
    fun findMaxId(): Int

    @Insert
    fun insertRecord(repo: RepositoryRepresentation)

    @Delete
    fun deleteRecord(repo: RepositoryRepresentation)

    @Query("select * from repository_representation_table order by id desc")
    fun getAllRecords(): MutableList<RepositoryRepresentation>

    @Query("select * from repository_representation_table where starred = 1 order by id desc")
    fun getStarredRecords(): MutableList<RepositoryRepresentation>

    @Query("delete from repository_representation_table")
    fun deleteAllRecords()
}
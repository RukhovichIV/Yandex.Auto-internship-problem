package com.example.yandexautointershipproblem.storing

import androidx.room.*

@Dao
interface RepoDatabaseDao {

    @Query("select count(1) from repository_representation_table where owner = :author and rep_title = :title")
    fun checkForRecord(author: String, title: String): Boolean

    @Query("select max(id) from repository_representation_table")
    fun findMaxId(): Int

    @Insert
    fun insertRecord(item: RepositoryRepresentation)

    @Delete
    fun deleteRecord(item: RepositoryRepresentation)

    @Query("select * from repository_representation_table order by id desc")
    fun getAllRecords(): List<RepositoryRepresentation>

    @Query("select * from repository_representation_table where starred = 1 order by id desc")
    fun getStarredRecords(): List<RepositoryRepresentation>

    @Query("delete from repository_representation_table")
    fun deleteAllRecords()
}
package com.example.yandexautointershipproblem.storing

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repr_table")
data class RepositoryRepresentation(

    @ColumnInfo(name = "rep_title")
    val title: String,

    @ColumnInfo(name = "rep_desc")
    val description: String,

    @ColumnInfo(name = "owner")
    val author: String,

    @ColumnInfo(name = "date_of_creation")
    val dateOfCreation: String,

    @ColumnInfo(name = "language")
    val language: String,

    @PrimaryKey
    val id: Int
)
package com.ghostdev.notesin.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_tb")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val content: String,

    val dateTime: Long,

    val color: Int
)
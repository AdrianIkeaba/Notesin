package com.ghostdev.notesin.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ghostdev.notesin.data.model.Note

@Dao
interface NoteDao {

    @Insert
    suspend fun addNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes_tb")
    suspend fun getAllNotes(): List<Note>

    @Query("""
        SELECT * FROM notes_tb
        WHERE title LIKE '%' || :search || '%'
        OR content LIKE '%' || :search || '%'
        ORDER BY 
            CASE WHEN title LIKE '%' || :search || '%' THEN 0 ELSE 1 END,
            CASE WHEN content LIKE '%' || :search || '%' THEN 0 ELSE 1 END
        """)
    suspend fun findNote(search: String): List<Note>

    @Query("SELECT * FROM notes_tb WHERE :id = id")
    suspend fun getNote(id: Int): Note
}
package com.ghostdev.notesin.repository

import com.ghostdev.notesin.data.database.NotesDB
import com.ghostdev.notesin.data.model.Note

class NoteRepository(private val notesDB: NotesDB) {

    suspend fun insertNote(note: Note) {
        notesDB.noteDao().addNote(note)
    }

    suspend fun deleteNote(note: Note) {
        notesDB.noteDao().deleteNote(note)
    }

    suspend fun updateNote(note: Note) {
        notesDB.noteDao().updateNote(note)
    }

    suspend fun getAllNotes(): List<Note> {
        return notesDB.noteDao().getAllNotes()
    }

    suspend fun findNote(search: String): List<Note> {
        return notesDB.noteDao().findNote(search)
    }

    suspend fun getNote(id: Int): Note {
        return notesDB.noteDao().getNote(id)
    }
}
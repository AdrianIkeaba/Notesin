package com.ghostdev.notesin.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ghostdev.notesin.data.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NotesDB: RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NotesDB? = null

        fun getInstance(context: Context): NotesDB {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        NotesDB::class.java,
                        "notes_db"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}
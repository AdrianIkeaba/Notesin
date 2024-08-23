package com.ghostdev.notesin.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghostdev.notesin.data.model.Note
import com.ghostdev.notesin.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeLogic(private val notesRepository: NoteRepository): ViewModel() {
    private val _notesList = MutableStateFlow<List<Note>>(emptyList())
    val notesList: StateFlow<List<Note>> = _notesList

    private val _noteItem = MutableStateFlow<Note?>(null)
    val noteItem: StateFlow<Note?> = _noteItem

    private val _selectedColor = MutableStateFlow(Color(0xFFFFBE0D))
    val selectedColor: StateFlow<Color> = _selectedColor

    init {
        getAllNotes()
    }

    private fun getAllNotes() {
        viewModelScope.launch {
            _notesList.value = notesRepository.getAllNotes()
        }
    }

    fun getNote(id: Int) {
        viewModelScope.launch {
            _noteItem.value = notesRepository.getNote(id)
            _selectedColor.value = Color(noteItem.value!!.color)
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            notesRepository.insertNote(note)
            getAllNotes()
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            notesRepository.updateNote(note)
            getAllNotes()
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            notesRepository.deleteNote(note)
            getAllNotes()
        }
    }

    fun selectColor(color: Color) {
        _selectedColor.value = color
    }

    fun searchNote(searchQuery: String) {
        viewModelScope.launch {
            _notesList.value = notesRepository.findNote(searchQuery)
        }
    }
}




val LocalHomeLogic = staticCompositionLocalOf<HomeLogic> {
    error("No HomeLogic provided")
}

@Composable
fun ProvideLogic(viewModel: HomeLogic, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalHomeLogic provides viewModel, content = content)
}
package com.ghostdev.notesin.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ghostdev.notesin.repository.NoteRepository

class HomeLogicFactory(private val notesRepository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeLogic::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeLogic(notesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
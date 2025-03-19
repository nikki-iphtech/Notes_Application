package com.example.notesapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NoteViewModel(private val noteDao: NoteDao) : ViewModel() {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    fun insert(note: Note) = viewModelScope.launch {
        noteDao.insert(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        noteDao.update(note)
    }

    fun delete(note: Note) = viewModelScope.launch {
        noteDao.delete(note)
    }

    companion object {
        fun provideFactory(noteDao: NoteDao): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return NoteViewModel(noteDao) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}
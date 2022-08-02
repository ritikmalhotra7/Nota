package com.example.nota.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nota.models.NoteRequest
import com.example.nota.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val notesRepository: NotesRepository):ViewModel() {

    val noteData = notesRepository.notesLiveData
    val noteList = notesRepository.notesList

    fun createNote(note:NoteRequest) = viewModelScope.launch(Dispatchers.IO) {
        notesRepository.getAllNotes()
    }
    fun updateNote(noteId:String, note:NoteRequest) = viewModelScope.launch(Dispatchers.IO) {
        notesRepository.updateNote(noteId, note)
    }
    fun deleteNote(noteId:String) = viewModelScope.launch(Dispatchers.IO) {
        notesRepository.deleteNote(noteId)
    }
    fun getNotes() = viewModelScope.launch(Dispatchers.IO) {
        notesRepository.getAllNotes()
    }
}
package com.example.nota.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nota.api.NotesApi
import com.example.nota.models.NoteRequest
import com.example.nota.models.NoteResponse
import com.example.nota.utils.Resource
import retrofit2.Response
import javax.inject.Inject


class NotesRepository @Inject constructor(private val notesApi: NotesApi) {

    private val _notesLiveData: MutableLiveData<Resource<NoteResponse>> = MutableLiveData()
    val notesLiveData: LiveData<Resource<NoteResponse>> get() = _notesLiveData

    private val _notesList: MutableLiveData<Resource<List<NoteResponse>>> = MutableLiveData()
    val notesList: LiveData<Resource<List<NoteResponse>>> get() = _notesList

    private val isDeleted: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val isNoteDeleted: LiveData<Resource<Boolean>> get() = isDeleted

    suspend fun createNote(note: NoteRequest) {
        _notesLiveData.postValue(Resource.Loading())
        val response = notesApi.createNote(note)
        handleNoteResponse(response)
    }

    suspend fun updateNote(noteId: String, note: NoteRequest) {
        _notesLiveData.postValue(Resource.Loading())
        val response = notesApi.updateNote(noteId, note)
        handleNoteResponse(response)
    }

    suspend fun deleteNote(noteId: String) {
        isDeleted.postValue(Resource.Loading())
        val response = notesApi.deleteNote(noteId)
        if (response.isSuccessful) {
            isDeleted.postValue(Resource.Success(true))
        } else {
            isDeleted.postValue(Resource.Error(response.message()))
        }
    }

    suspend fun getAllNotes() {
        _notesLiveData.postValue(Resource.Loading())
        val response = notesApi.getNotes()
        handleNotesResponse(response)
    }

    private fun handleNoteResponse(response: Response<NoteResponse>) {
        if (response.isSuccessful) {
            _notesLiveData.postValue(Resource.Success(response.body()!!))
        } else {
            _notesLiveData.postValue(Resource.Error(response.message()))
        }
    }

    private fun handleNotesResponse(response: Response<List<NoteResponse>>) {
        if (response.isSuccessful) {
            _notesList.postValue(Resource.Success(response.body()!!))
        } else {
            _notesList.postValue(Resource.Error(response.message()))
        }
    }
}
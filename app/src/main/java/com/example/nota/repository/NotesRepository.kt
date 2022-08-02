package com.example.nota.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nota.api.NotesApi
import com.example.nota.models.NoteRequest
import com.example.nota.models.NoteResponse
import com.example.nota.models.UserResponse
import com.example.nota.utils.Resource
import dagger.Provides
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


class NotesRepository @Inject constructor(private val notesApi:NotesApi) {

    private val _notesLiveData: MutableLiveData<Resource<NoteResponse>> = MutableLiveData()
    val notesLiveData: LiveData<Resource<NoteResponse>> get() = _notesLiveData

    private val _notesList:MutableLiveData<Resource<List<NoteResponse>>> = MutableLiveData()
    val notesList:LiveData<Resource<List<NoteResponse>>> get() = _notesList

    suspend fun createNote(note:NoteRequest) {
        _notesLiveData.postValue(Resource.Loading())
        val response = notesApi.createNote(note)
        handleNoteResponse(response)
    }
    suspend fun updateNote(noteId:String,note:NoteRequest){
        _notesLiveData.postValue(Resource.Loading())
        val response = notesApi.updateNote(noteId,note)
        handleNoteResponse(response)
    }
    suspend fun deleteNote(noteId:String){
        _notesLiveData.postValue(Resource.Loading())
        val response = notesApi.deleteNote(noteId)
        handleNoteResponse(response)
    }
    suspend fun getAllNotes() {
        _notesLiveData.postValue(Resource.Loading())
        val response = notesApi.getNotes()
        handleNotesResponse(response)
    }

    private fun handleNoteResponse(response: Response<NoteResponse>) {
        if (response.isSuccessful) {
            _notesLiveData.postValue(Resource.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObject =
                JSONObject(response.errorBody()!!.charStream().readText()).getString("message")
            _notesLiveData.postValue(Resource.Error(errorObject))
        } else {
            _notesLiveData.postValue(Resource.Error(response.message()))
        }
    }

    private fun handleNotesResponse(response: Response<List<NoteResponse>>) {
        if (response.isSuccessful) {
            _notesList.postValue(Resource.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObject =
                JSONObject(response.errorBody()!!.charStream().readText()).getString("message")
            _notesList.postValue(Resource.Error(errorObject))
        } else {
            _notesList.postValue(Resource.Error(response.message()))
        }
    }
}
package com.example.nota.api

import com.example.nota.models.NoteRequest
import com.example.nota.models.NoteResponse
import retrofit2.Response
import retrofit2.http.*

interface NotesApi {
    @GET("/notes/getNotes")
    suspend fun getNotes():Response<List<NoteResponse>>

    @POST("/notes/createNote")
    suspend fun createNote(@Body noteRequest:NoteRequest):Response<NoteResponse>

    @PUT("/notes/updateNotes/{id}")
    suspend fun updateNote(@Path("id") noteId:String,@Body noteRequest: NoteRequest):Response<NoteResponse>

    @DELETE("/notes/deleteNote/{id}")
    suspend fun deleteNote(@Path("id") noteId:String):Response<NoteResponse>

}
package com.example.nota.api

import com.example.nota.models.UserRequest
import com.example.nota.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("/users/signup")
    suspend fun signup(@Body userData:UserRequest): Response<UserResponse>

    @POST("/users/signin")
    suspend fun signin(@Body userData:UserRequest): Response<UserResponse>
}
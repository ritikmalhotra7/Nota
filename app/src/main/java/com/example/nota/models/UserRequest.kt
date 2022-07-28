package com.example.nota.models

data class UserRequest(
    val email: String,
    val password: String,
    val username: String? = null
)
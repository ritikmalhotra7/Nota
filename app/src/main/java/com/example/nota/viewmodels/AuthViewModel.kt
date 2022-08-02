package com.example.nota.viewmodels

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nota.models.UserRequest
import com.example.nota.models.UserResponse
import com.example.nota.repository.UserRepository
import com.example.nota.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    val userLiveData get() = userRepository.userLiveData

    fun registerUser(userRequest: UserRequest) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.registerUser(userRequest)
    }

    fun loginUser(userRequest: UserRequest) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.loginUser(userRequest)
    }

}
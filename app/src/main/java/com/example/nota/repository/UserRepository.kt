package com.example.nota.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nota.api.UserApi
import com.example.nota.models.UserRequest
import com.example.nota.models.UserResponse
import com.example.nota.utils.Resource
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {

    private val _userLiveData: MutableLiveData<Resource<UserResponse>> = MutableLiveData()
    val userLiveData: LiveData<Resource<UserResponse>> get() = _userLiveData

    suspend fun registerUser(userRequest: UserRequest) {
        _userLiveData.postValue(Resource.Loading())
        val response = userApi.signup(userRequest)
        handleResponse(response)
    }

    suspend fun loginUser(userRequest: UserRequest) {
        _userLiveData.postValue(Resource.Loading())
        val response = userApi.signin(userRequest)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful) {
            _userLiveData.postValue(Resource.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObject =
                JSONObject(response.errorBody()!!.charStream().readText()).getString("message")
            _userLiveData.postValue(Resource.Error(errorObject))
        } else {
            _userLiveData.postValue(Resource.Error(response.message()))
        }
    }
}
package com.example.howsapp.models

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howsapp.services.ApiClient
import com.example.howsapp.utils.ApiResult
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {

    //in this thing what we have done is tried to make this read only thing and is helpful for sensitive data
    private val _signUpResult = mutableStateOf<ApiResult<AuthResponse>?>(null)
    val signUpResult: State<ApiResult<AuthResponse>?> = _signUpResult

//    fun resetSignUpState() {
//        _signUpResult.value = null
//    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {

            try {
                _signUpResult.value = ApiResult.Loading
                val response = ApiClient.userApiService.signup(AuthRequest(email, password))
                if (response.isSuccessful) {
                    _signUpResult.value = ApiResult.Success(response.body()!!)
                } else {
                    _signUpResult.value =
                        ApiResult.Error(response.body()?.message ?: "Signup failed")
                }
            }catch (e:Exception){
                _signUpResult.value = ApiResult.Error(e.localizedMessage ?: "Unexpected error")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _signUpResult.value = ApiResult.Loading
                val response = ApiClient.userApiService.login(AuthRequest(email, password))
                if (response.isSuccessful) {
                    _signUpResult.value = ApiResult.Success(response.body()!!)
                } else {
                    _signUpResult.value =
                        ApiResult.Error(response.body()?.message ?: "Login failed")
                }


            } catch (e: Exception) {
                _signUpResult.value = ApiResult.Error(e.localizedMessage ?: "Unexpected error")
            }
        }


    }

    fun saveLoginStatus(context: Context, isLoggedIn: Boolean) {
        viewModelScope.launch {
            DataStoreManager.saveLoginStatus(context, isLoggedIn)
        }
    }

    fun saveUserEmail(context: Context, email: String) {
        viewModelScope.launch {
            DataStoreManager.saveUserEmail(context, email)
        }
    }

}

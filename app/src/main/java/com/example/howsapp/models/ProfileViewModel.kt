package com.example.howsapp.models

import DataStoreManager
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.howsapp.utils.ApiResult
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import com.example.howsapp.services.ApiClient


import kotlinx.coroutines.launch



class ProfileViewModel : ViewModel() {
    private val _profile = mutableStateOf<ApiResult<ProfileUpdateResponse>?>(null)
    val profile: State<ApiResult<ProfileUpdateResponse>?> = _profile





    fun updateProfile(name: String, phone: String, profileImage: String,email:String) {
        viewModelScope.launch {
            try {
                _profile.value = ApiResult.Loading
                    val response = ApiClient.userApiService.updateProfile(ProfileUpdateRequest(name, phone, profileImage,email))
                if (response.isSuccessful) {
                    Log.d("NETWORK", "Raw response: ${response.raw()}")
                    Log.d("ProfileUpdateResponse ", "Response: ${response.body().toString()}")
                    _profile.value = ApiResult.Success(response.body()!!)
                } else {
                    _profile.value = ApiResult.Error(response.body()?.message ?: "Profile update failed")
                }

            }catch(e:Exception){
                _profile.value = ApiResult.Error(e.localizedMessage ?: "Unexpected error")
                /**/
            }
        }
    }




    suspend fun getStoredEmail(context: Context): String? {
        return DataStoreManager.getUserEmail(context)
    }


}
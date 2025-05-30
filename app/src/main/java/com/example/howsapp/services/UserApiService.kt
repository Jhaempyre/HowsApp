package com.example.howsapp.services

import com.example.howsapp.models.AuthRequest
import com.example.howsapp.models.AuthResponse
import com.example.howsapp.models.ProfileUpdateRequest
import com.example.howsapp.models.ProfileUpdateResponse
import com.example.howsapp.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    @POST("/api/auth/register")
    suspend fun signup(@Body request: AuthRequest): Response<AuthResponse>

    @POST("/api/auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("/api/auth/updateProfile")
    suspend fun updateProfile(@Body request: ProfileUpdateRequest): Response<ProfileUpdateResponse>

}
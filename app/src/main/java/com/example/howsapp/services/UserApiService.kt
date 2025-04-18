package com.example.howsapp.services

import com.example.howsapp.models.AuthRequest
import com.example.howsapp.models.AuthResponse
import com.example.howsapp.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    @POST("/api/auth/register")
    suspend fun signup(@Body request: AuthRequest): Response<AuthResponse>

    @POST("/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>
}
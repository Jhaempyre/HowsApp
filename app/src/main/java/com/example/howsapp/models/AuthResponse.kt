package com.example.howsapp.models

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val user: User? = null
)

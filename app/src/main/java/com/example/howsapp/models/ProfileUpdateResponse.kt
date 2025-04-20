package com.example.howsapp.models

data class ProfileUpdateResponse(
    val success: Boolean,
    val message: String,
    val user : User
)
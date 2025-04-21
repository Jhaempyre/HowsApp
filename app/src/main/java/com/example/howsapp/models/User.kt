package com.example.howsapp.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val phone:String,
    val name: String,
    val profileImage: String,
    val id:String
    // add more fields if needed
)

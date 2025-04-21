package com.example.howsapp.models

import kotlinx.serialization.Serializable


@Serializable
data class Contact(val name: String, val phone: String)

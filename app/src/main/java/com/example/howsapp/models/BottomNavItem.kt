package com.example.howsapp.models

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val icon : ImageVector,
    val route: String,
)

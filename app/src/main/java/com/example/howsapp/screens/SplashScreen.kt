package com.example.howsapp.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.howsapp.Routes
import kotlinx.coroutines.delay
@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        delay(2000)

        val isLoggedIn = DataStoreManager.isLoggedIn(context)
        val permissionsAccepted = DataStoreManager.arePermissionsAccepted(context)

        if (permissionsAccepted) {
            // Permissions already granted, proceed to main flow
            if (isLoggedIn) {
                navController.navigate(Routes.MAIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            } else {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
        } else {
            // Navigate to permission screen first
            navController.navigate(Routes.permission) {
                popUpTo(Routes.SPLASH) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "HowsApp",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = Color.White.copy(alpha = alpha.value),
                fontWeight = FontWeight.Bold
            )
        )
    }
}
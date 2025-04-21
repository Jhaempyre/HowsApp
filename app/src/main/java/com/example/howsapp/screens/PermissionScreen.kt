package com.example.howsapp.screens

import DataStoreManager
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.howsapp.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PermissionScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Track permission status
    var contactsPermissionGranted by remember {
        mutableStateOf(isPermissionGranted(context, Manifest.permission.READ_CONTACTS))
    }
    var locationPermissionGranted by remember {
        mutableStateOf(isPermissionGranted(context, Manifest.permission.ACCESS_FINE_LOCATION))
    }
    var backgroundLocationPermissionGranted by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                isPermissionGranted(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                true // Not needed for older Android versions
            }
        )
    }

    // Permission explanation text
    var explanationText by remember { mutableStateOf("We need permissions to proceed") }

    // Launcher for contacts permission
    val contactsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        contactsPermissionGranted = isGranted
        if (isGranted) {
            // If contacts granted, proceed to request location
            explanationText = "We need location access to find nearby users"
        }
    }

    // Launcher for location permission
    val locationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationPermissionGranted = isGranted
        if (isGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // If location granted and API >= 29, update prompt for background location
            explanationText = "We need background location access to notify you of nearby users even when app is closed"
        } else if (isGranted) {
            // If location granted but older API, we're done
            completePermissionFlow(context, navController, scope)
        }
    }

    // Launcher for background location permission (only for API 29+)
    val backgroundLocationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        backgroundLocationPermissionGranted = isGranted
        // Even if denied, we proceed (it's optional)
        completePermissionFlow(context, navController, scope)
    }

    // Check if all permissions are granted on screen load
    LaunchedEffect(true) {
        if (contactsPermissionGranted && locationPermissionGranted && backgroundLocationPermissionGranted) {
            completePermissionFlow(context, navController, scope)
        }
    }

    // UI for permission requests
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(explanationText)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            when {
                !contactsPermissionGranted -> {
                    contactsLauncher.launch(Manifest.permission.READ_CONTACTS)
                }
                !locationPermissionGranted -> {
                    locationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
                !backgroundLocationPermissionGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    backgroundLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }
                else -> {
                    completePermissionFlow(context, navController, scope)
                }
            }
        }) {
            Text(getButtonText(contactsPermissionGranted, locationPermissionGranted, backgroundLocationPermissionGranted))
        }
    }
}

private fun getButtonText(
    contactsGranted: Boolean,
    locationGranted: Boolean,
    backgroundGranted: Boolean
): String {
    return when {
        !contactsGranted -> "Grant Contacts Permission"
        !locationGranted -> "Grant Location Permission"
        !backgroundGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> "Grant Background Location"
        else -> "Continue"
    }
}

private fun isPermissionGranted(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

private fun completePermissionFlow(context: Context, navController: NavHostController, scope: kotlinx.coroutines.CoroutineScope) {
    scope.launch {
        DataStoreManager.setPermissionsAccepted(context, true)
        navigateAfterPermissions(context, navController)
    }
}

private suspend fun navigateAfterPermissions(context: Context, navController: NavHostController) {
    val isLoggedIn = DataStoreManager.isLoggedIn(context)
    delay(500)
    if (isLoggedIn) {
        navController.navigate(Routes.MAIN) {
            popUpTo(Routes.permission) { inclusive = true }
        }
    } else {
        navController.navigate(Routes.LOGIN) {
            popUpTo(Routes.permission) { inclusive = true }
        }
    }
}
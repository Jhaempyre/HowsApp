package com.example.howsapp.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.howsapp.Routes
import com.example.howsapp.models.AuthViewModel
import com.example.howsapp.utils.ApiResult


@Composable
fun LoginCard(authViewModel: AuthViewModel = viewModel(),
              onLoginClick: (String, String) -> Unit,
              onSignUpClick: () -> Unit,
              navController: NavController
        ) {
    val context = LocalContext.current
    val signupResult by authViewModel.signUpResult
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Card(
        modifier = androidx.compose.ui.Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = androidx.compose.ui.Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Please Log In",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = androidx.compose.ui.Modifier.fillMaxWidth()
            )

            Spacer(modifier = androidx.compose.ui.Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = androidx.compose.ui.Modifier.fillMaxWidth()
            )

            Spacer(modifier = androidx.compose.ui.Modifier.height(20.dp))

            Button(
                onClick = { authViewModel.login(email, password) },
                modifier = androidx.compose.ui.Modifier,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Log In")
            }

            Spacer(modifier = androidx.compose.ui.Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = androidx.compose.ui.Modifier.fillMaxWidth()
            ) {
                Text(text = "Don't Have an account? ")
                Text(
                    text = "Sign Up",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = androidx.compose.ui.Modifier.clickable { onSignUpClick() }
                )
            }


        }
        when (signupResult) {
            is ApiResult.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }

            is ApiResult.Success -> {
                LaunchedEffect(Unit) {
                        authViewModel.saveLoginStatus(context, true)
                        authViewModel.saveUserEmail(context, email)
                        Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
                        navController.navigate(Routes.MAIN)
                    Toast.makeText(context, "Signup Success", Toast.LENGTH_SHORT).show()
                    navController.navigate(Routes.MAIN)
                }
            }

            is ApiResult.Error -> {
                Text(
                    text = (signupResult as ApiResult.Error).message,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            null -> { /* Initial state, show nothing */
            }
        }
    }

}

    @Composable
    fun LoginScreen(onSignUpNavigate: () -> Unit,
                    navController: NavHostController
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F6F6)),
            contentAlignment = Alignment.Center
        ) {
            LoginCard(
                onLoginClick = { email, password ->
                    // Handle login logic here
                    Log.d("Login", "Email: $email, Password: $password")
                },
                onSignUpClick = {
                    // Handle sign up click
                    onSignUpNavigate()
                    Log.d("Login", "Navigate to Sign Up")
                },
                navController = navController

            )
        }
    }




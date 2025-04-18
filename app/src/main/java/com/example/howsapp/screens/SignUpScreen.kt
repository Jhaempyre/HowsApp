package com.example.howsapp.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.example.howsapp.models.AuthViewModel
import com.example.howsapp.utils.ApiResult


@Composable
fun SignUpCard(
    authViewModel: AuthViewModel = viewModel(),
    onSignUpClick: (String, String) -> Unit,
    onLoginClick: () -> Unit,

){

    val context = LocalContext.current
    val signupResult by authViewModel.signUpResult

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(8.dp),

        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text="Sign Up",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold

                )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { authViewModel.signUp(email, password) },
                modifier = Modifier,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Sign Up")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(text = "Already have an account? ")
            }
            Text(
                text = "Log in",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onLoginClick()}
            )

        }

        when(signupResult){
            is ApiResult.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
            is ApiResult.Success -> {
                LaunchedEffect(Unit) {
                    Toast.makeText(context, "Signup Success", Toast.LENGTH_SHORT).show()
                    onLoginClick()
                }
            }
            is ApiResult.Error -> {
                Text(
                    text = (signupResult as ApiResult.Error).message,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            null -> { /* Initial state, show nothing */ }
        }
    }

}

@Composable
fun SignUpScreen( onLoginNavigate: () -> Unit){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6)),
        contentAlignment = Alignment.Center
    ){
        SignUpCard(
            onSignUpClick = { email, password ->
                // Handle sign up logic here
                Log.d("SignUp", "Email: $email, Password: $password")
            },
            onLoginClick = {
                // Handle login click
                onLoginNavigate()
                Log.d("SignUp", "Navigate to Login")
            }
        )
    }
}

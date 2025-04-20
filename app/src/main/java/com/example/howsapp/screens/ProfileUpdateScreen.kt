package com.example.howsapp.screens

import DataStoreManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.howsapp.Routes
import com.example.howsapp.models.AuthViewModel
import com.example.howsapp.models.ProfileViewModel
import com.example.howsapp.utils.ApiResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



@Composable
fun ProfileUpdateScreen(profileViewModel: ProfileViewModel = viewModel(),
                        onProfileUpdate: () -> Unit,
                        navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var profileImage by remember { mutableStateOf("") }
//    var emailState by remember { mutableStateOf<String?>(null) }
//    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val profile by profileViewModel.profile




//    LaunchedEffect(Unit) {
//        emailState = profileViewModel.getStoredEmail(context)
//    }  we kinda only preffer it while opening app or gonna really build
//    someting moreshinging like passing through paymnet gateway
    //not like this thus we will do scop.launch in button behaivour as it is even driven asynchronous calls are it's behaviour
    // i am fucking beast 🙄🙄🙄

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text(text = "Update Profile", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        OutlinedTextField(
            value = profileImage,
            onValueChange = { profileImage = it },
            label = { Text("Profile Picture URL") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
        )
        Button(
            onClick = {
                // You'll call your backend here
                // After successful update, call onProfileUpdated()
                //onProfileUpdated()
//                val email = DataStoreManager.getUserEmail(context)

                //scope.launch is something coroutines behaviours works wellinside enet driven artitetcure



                    scope.launch {

                        val email = profileViewModel.getStoredEmail(context)
                        Log.d("ProfileUpdateemail", "Email: $email")
                        email?.let {
                            profileViewModel.updateProfile(name, phone, profileImage, email)




                    }
                }
// This was a way to do this but it is not good to keep ui waiting for  api or suspend call. we will try something else

                Log.d("ProfileUpdate", "Name: $name, Phone: $phone, Profile Pic: $profileImage")
            },
            modifier = Modifier.fillMaxWidth()
        )
             {
                Text("Submit")
            }

        }

    when(profile){
        is ApiResult.Loading -> {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }
        is ApiResult.Success -> {
            LaunchedEffect(Unit) {
                profileViewModel.saveUser(context, (profile as ApiResult.Success).data.user)
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()
                navController.navigate(InternalRoutes.CHATS)

            }
        }
        is ApiResult.Error -> {
            Text(
                text = (profile as ApiResult.Error).message,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        null -> { /* Initial state, show nothing */}

    }




}







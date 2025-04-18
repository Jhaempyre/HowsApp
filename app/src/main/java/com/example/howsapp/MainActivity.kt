package com.example.howsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.howsapp.screens.LoginScreen
import com.example.howsapp.screens.SignUpScreen
import com.example.howsapp.screens.SplashScreen
import com.example.howsapp.ui.theme.HowsAppTheme



object Routes {
    const val SPLASH = "splash"
    const val SIGN_UP = "signup"
    const val LOGIN = "login"
}



@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SIGN_UP) {
            SignUpScreen(onLoginNavigate = {
                navController.navigate(Routes.LOGIN)
            })
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onSignUpNavigate = {
                    navController.navigate(Routes.SIGN_UP)
                }
            )
        }
        composable(Routes.SPLASH) {
            SplashScreen(navController)
        }
    }
}



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HowsAppTheme {
                AppNavigation()
            }
        }
    }
}







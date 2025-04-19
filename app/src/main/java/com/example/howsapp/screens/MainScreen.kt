package com.example.howsapp.screens

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.howsapp.models.BottomNavItem
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

object InternalRoutes{
    const val CHATS = "chats"
    const val STATUS = "status"
    const val CALLS = "calls"

}


@ExperimentalMaterial3Api
@Composable
fun MainScreen(onLogout:()->Unit,onProfileUpdate:()->Unit) {
    val navController = rememberNavController()

    val bottomItems = listOf(

        BottomNavItem(InternalRoutes.CHATS, icon = Icons.Default.Favorite, "chats"),
        BottomNavItem(InternalRoutes.STATUS, icon = Icons.Default.AddCircle, "status"),
        BottomNavItem(InternalRoutes.CALLS, icon = Icons.Default.Call, "calls")

    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HowsApp") },
                actions = {
                    var expanded by remember { mutableStateOf(false) }

                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Update Profile") },
                            onClick = {
                                expanded = false
                                onProfileUpdate()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Logout") },
                            onClick = {
                                expanded = false
                                onLogout()
                            }
                        )
                    }
                }
            )
        },

        bottomBar= {
            BottomNavigation(modifier = Modifier.navigationBarsPadding()) {

                val currentRoute =
                    navController.currentBackStackEntryAsState().value?.destination?.route

                bottomItems.forEach { item ->
                    BottomNavigationItem(selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = {
                            Text(item.label)

                        }
                    )
                }
            }
        }
    ){
        innerPadding ->
        NavHost(
            navController = navController,
            startDestination = InternalRoutes.CHATS,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(InternalRoutes.STATUS) { StatusScreen() }
            composable(InternalRoutes.CHATS) { ChatScreen() }
            composable(InternalRoutes.CALLS) { CallScreen() }
        }
    }
}






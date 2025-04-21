package com.example.howsapp.screens


import DataStoreManager
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.howsapp.Routes
import com.example.howsapp.models.Contact
import kotlinx.coroutines.flow.collect
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
@Composable
fun RecentChatsScreen(navController: NavHostController) {
    val context = LocalContext.current
    var contacts by remember { mutableStateOf(listOf<Contact>()) }

    LaunchedEffect(true) {
        DataStoreManager.getRecentChatsFlow(context).collect { contactList ->
            contacts = contactList
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(InternalRoutes.CONTACTS)
            }) {
                Icon(Icons.Default.Add, contentDescription = "New Chat")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)) {
            Text("Recent Chats", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            if (contacts.isEmpty()) {
                Text("No recent chats")
            } else {
                LazyColumn {
                    items(contacts) { contact ->
                        ContactCard(contact) {
                            navController.navigate("${Routes.CHATS}/${contact.name}/${contact.phone}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContactCard(contact: Contact, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(contact.name, style = MaterialTheme.typography.titleMedium)
            Text(contact.phone, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

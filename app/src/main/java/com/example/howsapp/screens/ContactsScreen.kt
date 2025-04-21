package com.example.howsapp.screens

import DataStoreManager
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.ContactsContract
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.howsapp.Routes
import com.example.howsapp.models.Contact
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(navController: NavHostController) {
    val context = LocalContext.current
    var contacts by remember { mutableStateOf<List<Contact>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    // Check if contact permission is granted
    val hasContactPermission = remember {
        ActivityCompat.checkSelfPermission(
            context, Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Load contacts
    LaunchedEffect(true) {
        if (hasContactPermission) {
            contacts = loadContacts(context)
            isLoading = false
        } else {
            // If permission is not granted, show a Toast and navigate back
            Toast.makeText(context, "Contacts permission is required.", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select a Contact") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(contacts) { contact ->
                    ContactItem(
                        contact = contact,
                        onClick = {
                            scope.launch {
                                DataStoreManager.addToRecentChats(context, contact)
                                navController.navigate("${InternalRoutes.CHATS}/${contact.name}/${contact.phone}")
                            }
                        }
                    )
                }
            }
        }
    }
}

    @Composable
    fun ContactItem(contact: Contact, onClick: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 12.dp)
        ) {
            Text(text = contact.name, style = MaterialTheme.typography.titleSmall)
            Text(text = contact.phone, style = MaterialTheme.typography.bodySmall)
            Divider(modifier = Modifier.padding(top = 8.dp))
        }
    }

    private fun loadContacts(context: Context): List<Contact> {
        val contactsList = mutableListOf<Contact>()
        val resolver = context.contentResolver
        val cursor = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex) ?: continue
                val number = it.getString(numberIndex) ?: continue
                contactsList.add(Contact(name, number))
            }
        }

        return contactsList.distinctBy { it.phone } // Avoid duplicates
    }




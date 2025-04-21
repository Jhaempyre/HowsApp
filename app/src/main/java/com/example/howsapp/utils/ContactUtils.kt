//package com.example.howsapp.screens
//
//import android.content.Context
//import android.provider.ContactsContract
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import com.example.howsapp.Routes
//
//data class Contact(val name: String, val phone: String)
//
//@Composable
//fun ContactsScreen(navController: NavHostController) {
//    val context = LocalContext.current
//    var contacts by remember { mutableStateOf<List<Contact>>(emptyList()) }
//
//    // Load contacts once
//    LaunchedEffect(true) {
//        contacts = loadContacts(context)
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text("Select a contact to start chatting", style = MaterialTheme.typography.titleMedium)
//        Spacer(modifier = Modifier.height(12.dp))
//
//        LazyColumn {
//            items(contacts) { contact ->
//                ContactItem(contact = contact) {
//                    // TODO: Add to RecentChats in future
//                    navController.navigate("${Routes.CHATS}/${contact.phone}")
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ContactItem(contact: Contact, onClick: () -> Unit) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onClick() }
//            .padding(vertical = 12.dp)
//    ) {
//        Text(text = contact.name, style = MaterialTheme.typography.titleSmall)
//        Text(text = contact.phone, style = MaterialTheme.typography.bodySmall)
//        Divider(modifier = Modifier.padding(top = 8.dp))
//    }
//}
//
//private fun loadContacts(context: Context): List<Contact> {
//    val contactsList = mutableListOf<Contact>()
//    val resolver = context.contentResolver
//    val cursor = resolver.query(
//        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//        null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
//    )
//
//    cursor?.use {
//        val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
//        val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
//
//        while (it.moveToNext()) {
//            val name = it.getString(nameIndex) ?: continue
//            val number = it.getString(numberIndex) ?: continue
//            contactsList.add(Contact(name, number))
//        }
//    }
//
//    return contactsList.distinctBy { it.phone } // Avoid duplicates
//}

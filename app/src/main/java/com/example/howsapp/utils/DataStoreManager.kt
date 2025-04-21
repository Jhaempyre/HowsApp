import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.howsapp.models.Contact
import com.example.howsapp.models.User

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json




object DataStoreManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

    // Keys
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    private val USER_EMAIL = stringPreferencesKey("user_email")
    private val USER_OBJECT = stringPreferencesKey("user_json")
    private val PERMISSION_ACCEPTED = booleanPreferencesKey("permission_accepted")
    private val RECENT_CHATS_KEY = stringSetPreferencesKey("recent_chats")

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }


    suspend fun saveLoginStatus(context: Context, isLoggedIn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = isLoggedIn
        }
    }

    suspend fun saveUserEmail(context: Context, email: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_EMAIL] = email
        }
    }

    suspend fun isLoggedIn(context: Context): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[IS_LOGGED_IN] ?: false
    }

    suspend fun getUserEmail(context: Context): String? {
        val prefs = context.dataStore.data.first()
        return prefs[USER_EMAIL]
    }

    /* New functions for User object */
    suspend fun saveUser(context: Context, user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_OBJECT] = Json.encodeToString(user)
            preferences[USER_EMAIL] = user.email
        }
    }

    suspend fun getUser(context: Context): User? {
        return context.dataStore.data.first()[USER_OBJECT]?.let {
            Json.decodeFromString<User>(it)
        }
    }

    // Flow version for reactive observation - switch from Gson to kotlinx.serialization
    fun getUserFlow(context: Context) = context.dataStore.data
        .map { preferences ->
            preferences[USER_OBJECT]?.let { userJson ->
                Json.decodeFromString<User>(userJson)
            }
        }


    suspend fun clearAll(context: Context) {
        context.dataStore.edit { it.clear() }
    }

    suspend fun setPermissionsAccepted(context: Context, accepted: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PERMISSION_ACCEPTED] = accepted
        }
    }

    suspend fun arePermissionsAccepted(context: Context): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[PERMISSION_ACCEPTED] ?: false
    }

    suspend fun addToRecentChats(context: Context, contact: Contact) {
        context.dataStore.edit { preferences ->
            val current = preferences[RECENT_CHATS_KEY]?.toMutableSet() ?: mutableSetOf()
            // Make sure Contact is properly serializable
            current.add(Json.encodeToString(contact))
            preferences[RECENT_CHATS_KEY] = current
        }
    }


    fun getRecentChatsFlow(context: Context): Flow<List<Contact>> {
        return context.dataStore.data
            .map { preferences ->
                preferences[RECENT_CHATS_KEY]?.mapNotNull { jsonString ->
                    runCatching {
                        Json.decodeFromString<Contact>(jsonString)
                    }.getOrNull()
                } ?: emptyList()
            }
    }
}


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.howsapp.models.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object DataStoreManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

    // Existing keys
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    private val USER_EMAIL = stringPreferencesKey("user_email")

    // New key for user object
    private val USER_OBJECT = stringPreferencesKey("user_json")

    /* Existing functions remain unchanged */
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
            // Convert User object to JSON
            val userJson = Gson().toJson(user)
            preferences[USER_OBJECT] = userJson

            // Keep email sync with existing functionality
            preferences[USER_EMAIL] = user.email
        }
    }

    suspend fun getUser(context: Context): User? {
        val prefs = context.dataStore.data.first()
        return prefs[USER_OBJECT]?.let { userJson ->
            Gson().fromJson(userJson, User::class.java)
        }
    }

    // Flow version for reactive observation
    fun getUserFlow(context: Context) = context.dataStore.data
        .map { preferences ->
            preferences[USER_OBJECT]?.let { userJson ->
                Gson().fromJson(userJson, User::class.java)
            }
        }


    suspend fun clearAll(context: Context) {
        context.dataStore.edit { it.clear() }
    }

}
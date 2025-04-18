import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit

import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first


object DataStoreManager{
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    private val USER_EMAIL = stringPreferencesKey("user_email")

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
}

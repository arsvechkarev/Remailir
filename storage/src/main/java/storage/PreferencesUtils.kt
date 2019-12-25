package storage

import android.content.Context

object PreferencesUtils {
  
  private const val PREFERENCES_FILE = "PREFERENCES_FILE"
  
  fun saveString(context: Context, key: String, value: String) {
    val preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)
    preferences.edit().putString(key, value).apply()
  }
  
  fun getString(context: Context, key: String): String? {
    val preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)
    return preferences.getString(key, null)
  }
  
  fun deleteAll(context: Context) {
    context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit().clear().apply()
  }
  
}
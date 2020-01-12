package storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import core.strings.SHARED_PREFERENCES_COMMON
import javax.inject.Inject

class SharedPreferencesManager @Inject constructor(context: Context) {
  
  private val prefs = context.getSharedPreferences(SHARED_PREFERENCES_COMMON, MODE_PRIVATE)
  
  fun putString(key: String, value: String) {
    prefs.edit().putString(key, value).apply()
  }
  
  fun getString(key: String): String? {
    return prefs.getString(key, null)
  }
  
  fun clear() {
    prefs.edit().clear().apply()
  }
  
}
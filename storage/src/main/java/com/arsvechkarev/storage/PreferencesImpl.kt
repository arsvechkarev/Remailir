package com.arsvechkarev.storage

import android.content.Context
import android.content.SharedPreferences

class PreferencesImpl(context: Context) : Preferences {
  private val preferences: SharedPreferences =
    context.getSharedPreferences(prefsFilename, Context.MODE_PRIVATE)
  
  override fun saveString(key: String, value: String) {
    preferences.edit().putString(key, value).apply()
  }
  
  override fun getString(key: String, defaultValue: String): String {
    return preferences.getString(key, defaultValue)!!
  }
  
  override fun getString(key: String): String? {
    return preferences.getString(key, null)
  }
  
  companion object {
    
    private const val prefsFilename = "CommonPreferences"
  }
}
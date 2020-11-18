package com.arsvechkarev.firebase.auth

import android.content.Context
import android.content.Context.MODE_PRIVATE

class SharedPrefsProfileSaver(context: Context) : ProfileSaver {
  
  private val sharedPrefs = context.getSharedPreferences(PROFILE_SAVER_FILENAME, MODE_PRIVATE)
  
  override fun getEmail(): String? {
    return sharedPrefs.getString(EMAIL, null)
  }
  
  override fun getUsername(): String? {
    return sharedPrefs.getString(USERNAME, null)
  }
  
  override fun saveEmail(email: String) {
    sharedPrefs.edit().putString(EMAIL, email).apply()
  }
  
  override fun saveUsername(username: String) {
    sharedPrefs.edit().putString(USERNAME, username).apply()
  }
  
  companion object {
    
    const val PROFILE_SAVER_FILENAME = "ProfileFile"
    const val EMAIL = "email"
    const val USERNAME = "username"
  }
}
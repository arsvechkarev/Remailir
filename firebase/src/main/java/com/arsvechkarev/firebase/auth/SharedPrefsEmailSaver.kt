package com.arsvechkarev.firebase.auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE

class SharedPrefsEmailSaver(context: Context) : EmailSaver {
  
  private val sharedPrefs = context.getSharedPreferences(PROFILE_SAVER_FILENAME, MODE_PRIVATE)
  
  override fun getEmail(): String? {
    return sharedPrefs.getString(EMAIL, null)
  }
  
  override fun saveEmail(email: String) {
    sharedPrefs.edit().putString(EMAIL, email).apply()
  }
  
  @SuppressLint("ApplySharedPref")
  override fun deleteEmailSynchronously() {
    sharedPrefs.edit().clear().commit()
  }
  
  companion object {
    
    const val PROFILE_SAVER_FILENAME = "ProfileFile"
    const val EMAIL = "email"
  }
}
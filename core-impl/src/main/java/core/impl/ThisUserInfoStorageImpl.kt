package core.impl

import android.app.Application
import android.content.Context.MODE_PRIVATE
import core.Dispatchers
import core.ThisUserInfoStorage
import core.model.ThisUserInfo
import kotlinx.coroutines.withContext

class ThisUserInfoStorageImpl(
  private val app: Application,
  private val dispatchers: Dispatchers
) : ThisUserInfoStorage {
  
  private val sharedPrefs by lazy { app.getSharedPreferences(SHARED_PREFS_FILENAME, MODE_PRIVATE) }
  
  private val thisUserInfo: ThisUserInfo by lazy {
    val username = sharedPrefs.getString(KEY_USERNAME, null) ?: error("Username is not saved")
    val email = sharedPrefs.getString(KEY_EMAIL, null) ?: error("Email is not saved")
    ThisUserInfo(username, email)
  }
  
  override fun getUserInfo(): ThisUserInfo {
    return thisUserInfo
  }
  
  override suspend fun saveUsername(username: String) {
    sharedPrefs.edit().putString(KEY_USERNAME, username).commit()
  }
  
  override suspend fun saveEmail(email: String) {
    sharedPrefs.edit().putString(KEY_EMAIL, email).commit()
  }
  
  override suspend fun getEmailOrNull(): String? = withContext(dispatchers.IO) {
    sharedPrefs.getString(KEY_EMAIL, null)
  }
  
  override suspend fun clear(): Unit = withContext(dispatchers.IO) {
    sharedPrefs.edit().clear().commit()
  }
  
  companion object {
    
    const val KEY_EMAIL = "Email"
    const val KEY_USERNAME = "Username"
    const val SHARED_PREFS_FILENAME = "ThisUserInfoFile"
  }
}
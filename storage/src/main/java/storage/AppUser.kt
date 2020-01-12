package storage

import android.content.Context
import core.model.users.User
import firebase.utils.thisUser
import log.debug

object AppUser {
  
  private const val KEY_USERNAME = "KEY_USERNAME"
  private const val KEY_IMAGE_URL = "KEY_IMAGE_URL"
  
  private var user: User? = null
  
  fun set(user: User, preferencesManager: SharedPreferencesManager) {
    debug { "user set" }
    this.user = user
    preferencesManager.putString(KEY_USERNAME, user.name)
    preferencesManager.putString(KEY_IMAGE_URL, user.imageUrl)
  }
  
  fun get(): User {
    if (user != null) {
      return user!!
    }
    throw IllegalStateException("User hasn't been set")
  }
  
  fun isUserSaved(context: Context): Boolean {
    val preferencesManager = SharedPreferencesManager(context)
    return preferencesManager.getString(KEY_USERNAME) != null
  }
  
  fun clear(context: Context) {
    val preferencesManager = SharedPreferencesManager(context)
    preferencesManager.clear()
  }
  
  fun retrieve(context: Context) {
    val preferencesManager = SharedPreferencesManager(context)
    val username = preferencesManager.getString(KEY_USERNAME)!!
    val imageUrl = preferencesManager.getString(KEY_IMAGE_URL)!!
    
    this.user = User(thisUser.uid, thisUser.phoneNumber!!, username, imageUrl)
  }
}
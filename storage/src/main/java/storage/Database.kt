package storage

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import core.model.users.User

object Database {
  
  private const val USERNAME = "USERNAME"
  private const val IMAGE_URL = "IMAGE_URL"
  
  fun saveUser(context: Context, username: String, imageUrl: String) {
    PreferencesUtils.saveString(context, USERNAME, username)
    PreferencesUtils.saveString(context, IMAGE_URL, imageUrl)
  }
  
  fun getUser(context: Context): User? {
    val username = PreferencesUtils.getString(context, USERNAME) ?: return null
    val imageUrl = PreferencesUtils.getString(context, IMAGE_URL)!!
    return User(
      FirebaseAuth.getInstance().currentUser!!.uid,
      FirebaseAuth.getInstance().currentUser!!.phoneNumber!!,
      username,
      imageUrl
    )
  }
  
  fun deleteAll(context: Context) {
    PreferencesUtils.deleteAll(context)
  }
  
}
package com.arsvechkarev.storage

import android.content.ContentUris
import android.content.Context
import com.arsvechkarev.core.model.users.NewUser
import com.google.firebase.auth.FirebaseAuth

object Database {
  
  const val USERNAME = "USERNAME"
  const val IMAGE_URL= "IMAGE_URL"
  
  fun saveUser(context: Context, username: String, imageUrl: String) {
    PreferencesUtils.saveString(context, USERNAME, username)
    PreferencesUtils.saveString(context, IMAGE_URL, imageUrl)
  }
  
  fun getUser(context: Context): NewUser? {
    val username = PreferencesUtils.getString(context, USERNAME) ?: return null
    val imageUrl = PreferencesUtils.getString(context, IMAGE_URL)!!
    return NewUser(
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
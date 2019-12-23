package com.arsvechkarev.profile.repositories

import core.model.users.User
import core.strings.FILENAME_USER
import firebase.Collections
import storage.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ProfileRepository @Inject constructor(
  private val storage: Storage
) {
  
  suspend fun fetchProfileData(listenerBlock: UserInfoListener.() -> Unit) {
    val listener = UserInfoListener().apply(listenerBlock)
    
    val savedUser = storage.get<User>(FILENAME_USER)
    if (savedUser != null) {
      listener.successBlock(savedUser)
      return
    }
    
    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    FirebaseFirestore.getInstance().collection(Collections.Users)
      .document(uid)
      .get()
      .addOnSuccessListener {
        val user = it.toObject(User::class.java)!!
        storage.saveSwitching(user, FILENAME_USER)
        listener.successBlock(user)
      }
      .addOnFailureListener {
        listener.failureBlock(it)
      }
  }
  
  class UserInfoListener {
  
    lateinit var successBlock: (User) -> Unit
    lateinit var failureBlock: (Exception) -> Unit
    
    fun onSuccess(successBlock: (User) -> Unit) {
      this.successBlock = successBlock
    }
    
    fun onFailure(failureBlock: (Exception) -> Unit) {
      this.failureBlock = failureBlock
    }
    
  }
}
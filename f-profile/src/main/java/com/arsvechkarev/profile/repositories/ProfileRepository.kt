package com.arsvechkarev.profile.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import core.model.users.User
import firebase.Collections
import javax.inject.Inject

class ProfileRepository @Inject constructor(
) {
  
  fun fetchProfileData(listenerBlock: UserInfoListener.() -> Unit) {
    val listener = UserInfoListener().apply(listenerBlock)
    
    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    FirebaseFirestore.getInstance().collection(Collections.Users)
      .document(uid)
      .get()
      .addOnSuccessListener {
        val user = it.toObject(User::class.java)!!
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
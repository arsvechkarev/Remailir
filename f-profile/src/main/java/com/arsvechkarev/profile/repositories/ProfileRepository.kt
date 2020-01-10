package com.arsvechkarev.profile.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import core.model.users.User
import firebase.RetrieveListener
import firebase.schema.Collections
import javax.inject.Inject

class ProfileRepository @Inject constructor() {
  
  fun fetchProfileData(block: RetrieveListener<User>.() -> Unit) {
    val listener = RetrieveListener<User>().apply(block)
    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    FirebaseFirestore.getInstance().collection(Collections.Users)
      .document(uid)
      .get()
      .addOnSuccessListener {
        val user = it.toObject(User::class.java)!!
        listener.success(user)
      }
      .addOnFailureListener {
        listener.failure(it)
      }
  }
}
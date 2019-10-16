package com.arsvechkarev.profile.repositories

import android.util.Log
import com.arsvechkarev.core.model.User
import com.arsvechkarev.firebase.Schema
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ProfileRepository @Inject constructor() {
  
  fun fetchProfileData(onSuccess: (DocumentSnapshot) -> Unit, onFailure: (Throwable) -> Unit) {
    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    FirebaseFirestore.getInstance().collection(Schema.USERS)
      .document(uid)
      .get()
      .addOnSuccessListener { onSuccess(it) }
      .addOnFailureListener { onFailure(it) }
  }
}
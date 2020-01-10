package com.arsvechkarev.profile.repositories

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import core.model.users.User
import core.model.users.withNewImageUrl
import firebase.RetrieveListener
import firebase.UploadListener
import firebase.schema.Collections.Users
import firebase.utils.getProfilePhotoPath
import firebase.utils.thisUser
import javax.inject.Inject

class ProfileRepository @Inject constructor(
  private val auth: FirebaseAuth,
  private val firestore: FirebaseFirestore,
  private val storage: FirebaseStorage
) {
  
  private var user: User = User.empty()
  
  fun fetchProfileData(block: RetrieveListener<User>.() -> Unit) {
    val listener = RetrieveListener<User>().apply(block)
    val uid = auth.currentUser!!.uid
    firestore.collection(Users)
      .document(uid)
      .get()
      .addOnSuccessListener {
        user = it.toObject(User::class.java)!!
        listener.success(user)
      }
      .addOnFailureListener {
        listener.failure(it)
      }
  }
  
  fun uploadImage(uri: Uri, block: UploadListener.() -> Unit) {
    val listener = UploadListener().apply(block)
    val child = storage.reference.child(getProfilePhotoPath())
    child.putFile(uri)
      .addOnSuccessListener {
        child.downloadUrl.addOnCompleteListener { task: Task<Uri> ->
          if (task.isSuccessful) {
            val url = task.result
            firestore.collection(Users).document(thisUser.uid)
              .set(user.withNewImageUrl(url.toString()))
              .addOnCompleteListener {
                listener.success()
              }
          }
        }
      }
      .addOnFailureListener {
        listener.failure(it)
      }
  }
}
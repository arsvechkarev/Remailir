package com.arsvechkarev.profile.repositories

import android.graphics.Bitmap
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import core.model.users.User
import core.model.users.withNewImageUrl
import durdinapps.rxfirebase2.RxFirestore
import firebase.RetrieveListener
import firebase.UploadListener
import firebase.schema.Collections.Users
import firebase.utils.getProfilePhotoPath
import firebase.utils.thisUser
import io.reactivex.Maybe
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ProfileRepository @Inject constructor(
  private val auth: FirebaseAuth,
  private val firestore: FirebaseFirestore,
  private val storage: FirebaseStorage
) {
  
  private var user: User = User.empty()
  
  fun fetchProfileDataRx(): Maybe<User> {
    val uid = auth.currentUser!!.uid
    return RxFirestore.getDocument(firestore.collection(Users).document(uid))
      .cast(User::class.java)
  }
  
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
  
  fun uploadImage(bitmap: Bitmap, block: UploadListener.() -> Unit) {
    val listener = UploadListener().apply(block)
    val child = storage.reference.child(getProfilePhotoPath())
    
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    
    child.putBytes(outputStream.toByteArray())
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
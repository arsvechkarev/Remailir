package com.arsvechkarev.profile.repositories

import android.graphics.Bitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import core.model.users.User
import core.model.users.withNewImageUri
import durdinapps.rxfirebase2.RxFirebaseStorage
import durdinapps.rxfirebase2.RxFirestore
import firebase.schema.Collections.Users
import firebase.utils.getProfilePhotoPath
import firebase.utils.thisUser
import io.reactivex.Completable
import io.reactivex.Maybe
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ProfileRepository @Inject constructor(
  private val firestore: FirebaseFirestore,
  private val storage: FirebaseStorage
) {
  
  private var user: User = User.empty()
  
  fun fetchProfileDataRx(): Maybe<User> {
    return RxFirestore.getDocument(firestore.collection(Users).document(thisUser.uid))
      .map { it.toObject(User::class.java)!! }
      .doOnSuccess { user = it }
  }
  
  fun uploadImageRx(bitmap: Bitmap): Completable {
    val child = storage.reference.child(getProfilePhotoPath())
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    val bytes = outputStream.toByteArray()
    
    return RxFirebaseStorage.putBytes(child, bytes)
      .flatMap { RxFirebaseStorage.getDownloadUrl(child).toSingle() }
      .flatMapCompletable {
        RxFirestore.setDocument(
          firestore.collection(Users).document(thisUser.uid),
          user.withNewImageUri(it)
        )
      }
  }
}
package com.arsvechkarev.profile.repositories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import core.RxJavaSchedulersProvider
import core.strings.profilePictureFile
import io.reactivex.Completable
import io.reactivex.Maybe
import java.io.FileOutputStream
import javax.inject.Inject

class ProfileDiskStorage @Inject constructor(
  private val schedulersProvider: RxJavaSchedulersProvider
) {
  
  fun getProfileImage(): Maybe<Bitmap> {
    return Maybe.create { emmiter ->
      try {
        if (!profilePictureFile.exists()) {
          emmiter.onComplete()
        } else {
          val bitmap = BitmapFactory.decodeFile(profilePictureFile.toString())
          emmiter.onSuccess(bitmap)
        }
      } catch (e: Exception) {
        emmiter.onError(e)
      }
    }
  }
  
  fun saveProfileImage(bitmap: Bitmap) = Completable.create { emitter ->
    try {
      val file = profilePictureFile
      if (!file.exists()) {
        file.parentFile.mkdirs()
        file.createNewFile()
      }
      FileOutputStream(file).use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, it)
      }
    } catch (e: Exception) {
      emitter.onError(e)
    }
  }
}
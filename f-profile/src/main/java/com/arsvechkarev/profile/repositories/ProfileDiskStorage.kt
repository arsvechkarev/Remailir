package com.arsvechkarev.profile.repositories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import core.RxJavaSchedulersProvider
import core.strings.appPicturesPath
import core.strings.profilePictureFile
import io.reactivex.Completable
import io.reactivex.Maybe
import log.Loggable
import log.log
import java.io.FileOutputStream
import javax.inject.Inject


class ProfileDiskStorage @Inject constructor(
  private val schedulersProvider: RxJavaSchedulersProvider
) : Loggable {
  
  override val logTag = "ProfileStuff"
  
  fun getProfileImage(): Maybe<Bitmap> {
    return Maybe.create { emmiter ->
      try {
        if (!profilePictureFile.exists()) {
          log { "try to search path: $profilePictureFile" }
          log { "file is not exist, return complete" }
          emmiter.onComplete()
        } else {
          val bitmap = BitmapFactory.decodeFile(profilePictureFile.toString())
          if (bitmap == null) {
            log { "bitmap == null, return complete" }
            emmiter.onComplete()
          } else {
            log { "found image on disk" }
            emmiter.onSuccess(bitmap)
          }
        }
      } catch (e: Exception) {
        log(e) { "error while loading from disk" }
        emmiter.onError(e)
      }
    }
  }
  
  fun saveProfileImage(bitmap: Bitmap) = Completable.create { emitter ->
    try {
      if (!appPicturesPath.exists()) {
        appPicturesPath.mkdirs()
      }
      val pictureFile = profilePictureFile
      if (pictureFile.exists()) {
        pictureFile.delete()
      } else {
        pictureFile.createNewFile()
      }
      FileOutputStream(pictureFile).use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, it)
      }
      log { "bitmap is saved to storage" }
      emitter.onComplete()
    } catch (e: Exception) {
      log(e) { "error while uploading to disk" }
      emitter.onError(e)
    }
  }
}
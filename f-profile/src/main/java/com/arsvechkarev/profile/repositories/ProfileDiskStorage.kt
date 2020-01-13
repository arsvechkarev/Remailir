package com.arsvechkarev.profile.repositories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import core.strings.appPicturesPath
import core.strings.profilePictureFile
import io.reactivex.Completable
import io.reactivex.Maybe
import log.Loggable
import log.log
import storage.SharedPreferencesManager
import java.io.FileOutputStream
import javax.inject.Inject


class ProfileDiskStorage @Inject constructor(
  private val preferencesManager: SharedPreferencesManager
) : Loggable {
  
  override val logTag = "ProfileStuff"
  
  fun getProfileImage(): Maybe<Bitmap> {
    return Maybe.create { emmiter ->
      try {
        if (!profilePictureFile.exists()) {
          log { "File is not exist, return onComplete()" }
          emmiter.onComplete()
          return@create
        }
        if (preferencesManager.isTrue(SAVED_IMAGE_AT_LEAST_ONCE)) {
          val bitmap = BitmapFactory.decodeFile(profilePictureFile.toString())
          if (bitmap == null) {
            log { "bitmap == null, return onComplete()" }
            emmiter.onComplete()
          } else {
            log { "Found image on disk, return onSuccess()" }
            emmiter.onSuccess(bitmap)
          }
        } else {
          // User hasn't loaded photo yet, therefore found file might be cache from
          // previous uploads. In this case return Maybe.onComplete() to indicate that
          // to get actual profile image user need to make request to the network
          emmiter.onComplete()
        }
      } catch (e: Exception) {
        log(e) { "Error while loading from disk" }
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
      preferencesManager.putBoolean(SAVED_IMAGE_AT_LEAST_ONCE, true)
      log { "Bitmap is successfully saved to storage" }
      emitter.onComplete()
    } catch (e: Exception) {
      log(e) { "Error while uploading to disk" }
      emitter.onError(e)
    }
  }
  
  companion object {
    
    private const val SAVED_IMAGE_AT_LEAST_ONCE = "SAVED_IMAGE_FIRST_TIME"
  }
}
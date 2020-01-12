package com.arsvechkarev.profile.repositories

import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import core.strings.profilePictureFile
import io.reactivex.Completable
import io.reactivex.Maybe
import log.Loggable
import log.debug
import storage.UploadImageWorker
import javax.inject.Inject

class ProfileRepository @Inject constructor(
  private val firebaseStorage: FirebaseStorage,
  private val uploadImageWorker: UploadImageWorker,
  private val profileDiskStorage: ProfileDiskStorage,
  private val profileNetworkRequests: ProfileNetworkRequests
) : Loggable {
  
  override val logTag = "ProfileStuffRepository"
  
  fun getProfileImage(url: String): Maybe<Bitmap> {
    val diskData = profileDiskStorage.getProfileImage()
    val networkData = profileNetworkRequests.loadProfileImage(url)
      .doOnSuccess { bitmap ->
        profileDiskStorage.saveProfileImage(bitmap)
          .subscribe(
            { debug { "image loaded to disk" } },
            { debug(it) { "nope" } })
      }
    
    return Maybe.concat(diskData, networkData)
      .firstElement()
  }
  
  fun uploadImageRx(bitmap: Bitmap): Completable {
    return profileDiskStorage.saveProfileImage(bitmap)
      .andThen { uploadImageWorker.uploadImage(profilePictureFile.path) }
  }
}
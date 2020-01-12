package com.arsvechkarev.profile.repositories

import android.graphics.Bitmap
import core.strings.profilePictureFile
import io.reactivex.Completable
import io.reactivex.Maybe
import log.Loggable
import log.log
import storage.UploadImageWorker
import javax.inject.Inject

class ProfileRepository @Inject constructor(
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
            { log { "Image was loaded to the disk" } },
            { log(it) { "Error while loading to disk" } }
          )
      }.doOnError {
        log(it) { "Error happened" }
      }
    
    return Maybe.concat(diskData, networkData)
      .firstElement()
  }
  
  fun uploadImageRx(bitmap: Bitmap): Completable {
    return profileDiskStorage.saveProfileImage(bitmap)
      .andThen { uploadImageWorker.uploadImage(profilePictureFile.path) }
  }
}
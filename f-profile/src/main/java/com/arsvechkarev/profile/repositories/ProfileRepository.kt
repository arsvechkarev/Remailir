package com.arsvechkarev.profile.repositories

import android.graphics.Bitmap
import io.reactivex.Completable
import io.reactivex.Maybe
import log.Loggable
import log.debug
import storage.UploadImageWorker
import javax.inject.Inject

class ProfileRepository @Inject constructor(
  private val uploadImageWorker: UploadImageWorker,
  private val profileDiskStorage: ProfileDiskStorage,
  private val profileNetworkRequests: ProfileNetworkRequests
) : Loggable {
  
  override val logTag = "ProfileStuffRepository"
  
  private var profileImage: Bitmap? = null
  
  fun getProfileImage(url: String): Maybe<Bitmap> {
    if (profileImage != null) {
      return Maybe.just(profileImage!!)
    }
    val diskData = profileDiskStorage.getProfileImage().doOnSuccess { profileImage = it }
    val networkData = profileNetworkRequests.loadProfileImage(url)
      .doOnSuccess {
        profileImage = it
        profileDiskStorage.saveProfileImage(it).subscribe({
          debug { "image loaded to disk" }
        }, {
          debug { "nope" }
        })
      }
    
    return Maybe.concat(diskData, networkData)
      .firstElement()
  }
  
  fun uploadImageRx(localPath: String, bitmap: Bitmap): Completable {
    //    uploadImageWorker.uploadImage(localPath)
    return profileDiskStorage.saveProfileImage(bitmap)
  }
}
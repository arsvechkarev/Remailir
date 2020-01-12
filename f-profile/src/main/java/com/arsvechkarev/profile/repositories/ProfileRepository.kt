package com.arsvechkarev.profile.repositories

import android.graphics.Bitmap
import io.reactivex.Maybe
import storage.AppUser
import storage.UploadImageWorker
import javax.inject.Inject

class ProfileRepository @Inject constructor(
  private val uploadImageWorker: UploadImageWorker,
  private val profileDiskStorage: ProfileDiskStorage,
  private val profileNetworkRequests: ProfileNetworkRequests
) {
  
  private var profileImage: Bitmap? = null
  
  fun getProfileImage(url: String): Maybe<Bitmap> {
    if (profileImage != null) {
      return Maybe.just(profileImage!!)
    }
    val diskData = profileDiskStorage.getProfileImage().doOnSuccess { profileImage = it }
    val networkData = profileNetworkRequests.loadProfileImage(url)
      .doOnSuccess {
        profileImage = it
        profileDiskStorage.saveProfileImage(it)
      }
    
    return Maybe.concat(diskData, networkData)
      .firstElement()
  }
  
  fun uploadImageRx(uri: String) {
    uploadImageWorker.uploadImage(uri, AppUser.get())
  }
}
package com.arsvechkarev.profile.presentation

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.profile.repositories.ProfileRepository
import core.MaybeResult
import core.RxJavaSchedulersProvider
import core.base.RxViewModel
import log.Loggable
import log.log
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
  private val schedulersProvider: RxJavaSchedulersProvider,
  private val repository: ProfileRepository
) : RxViewModel(), Loggable {
  
  override val logTag = "ProfileStuff"
  
  val profileImageState = MutableLiveData<MaybeResult<Bitmap>>()
  
  fun fetchProfileImage(url: String) {
    log { "start fetching image" }
    rxCall {
      repository.getProfileImage(url)
        .subscribeOn(schedulersProvider.io)
        .observeOn(schedulersProvider.mainThread)
        .subscribe({
          profileImageState.value = MaybeResult.success(it)
        }, {
          profileImageState.value = MaybeResult.failure(it)
        }, {
          profileImageState.value = MaybeResult.nothing()
        })
    }
  }
  
  fun uploadImageRx(bitmap: Bitmap) {
    log { "Start uploading image" }
    rxCall {
      repository.uploadImageRx(bitmap)
        .subscribeOn(schedulersProvider.io)
        .subscribe({
          log { "Image has been loaded to disk" }
        }, {
          log(it) { "Image hasn't been uploaded to disk" }
        })
    }
  }
  
}
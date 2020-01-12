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
  
  fun uploadImageRx(uri: String, bitmap: Bitmap) {
    log { "start uploading image" }
    rxCall {
      repository.uploadImageRx(uri, bitmap)
        .subscribeOn(schedulersProvider.io)
        .subscribe({
          log { "image loaded to disk" }
        }, {
          log(it) { "image hasn't been uploaded to disk" }
        })
    }
  }
  
}
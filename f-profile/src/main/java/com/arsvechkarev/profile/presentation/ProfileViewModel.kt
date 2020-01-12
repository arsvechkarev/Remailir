package com.arsvechkarev.profile.presentation

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.profile.repositories.ProfileRepository
import core.MaybeResult
import core.RxJavaSchedulersProvider
import core.base.RxViewModel
import core.strings.DEFAULT_IMG_URL
import log.debug
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
  private val schedulersProvider: RxJavaSchedulersProvider,
  private val repository: ProfileRepository
) : RxViewModel() {
  
  val profileImageState = MutableLiveData<MaybeResult<Bitmap>>()
  
  fun fetchProfileImage(url: String) {
    if (url == DEFAULT_IMG_URL) {
      profileImageState.value = MaybeResult.nothing()
    } else {
      rxCall {
        repository.getProfileImage(url)
          .subscribeOn(schedulersProvider.io)
          .observeOn(schedulersProvider.mainThread)
          .subscribe({
            profileImageState.value = MaybeResult.success(it)
          }, {
            profileImageState.value = MaybeResult.failure(it)
          })
      }
    }
  }
  
  fun uploadImageRx(uri: String, bitmap: Bitmap) {
    rxCall {
      repository.uploadImageRx(uri, bitmap)
        .subscribeOn(schedulersProvider.io)
        .subscribe {
          debug { "image loaded to disk" }
        }
    }
  }
  
}
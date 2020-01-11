package com.arsvechkarev.profile.presentation

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.profile.repositories.ProfileRepository
import core.RawResult
import core.base.RxViewModel
import core.model.users.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
  private val repository: ProfileRepository
) : RxViewModel() {
  
  val userDataState = MutableLiveData<Result<User>>()
  val uploadingImageState = MutableLiveData<RawResult>()
  
  fun fetchProfileDataRx() {
    rxCall {
      repository.fetchProfileDataRx()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          userDataState.value = Result.success(it)
        }
    }
  }
  
  fun uploadImageRx(bitmap: Bitmap) {
    rxCall {
      repository.uploadImageRx(bitmap)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
          { uploadingImageState.value = RawResult.success() },
          { uploadingImageState.value = RawResult.failure(it) }
        )
    }
  }
  
}
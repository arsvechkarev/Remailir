package com.arsvechkarev.profile.presentation

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arsvechkarev.profile.repositories.ProfileRepository
import core.RawResult
import core.model.users.User
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
  private val repository: ProfileRepository
) : ViewModel() {
  
  var userDataState = MutableLiveData<Result<User>>()
  var uploadingImageState = MutableLiveData<RawResult>()
  
  fun fetchProfileDataRx(): Maybe<User> {
    return repository.fetchProfileDataRx()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
  }
  
  fun fetchProfileData() {
    repository.fetchProfileData {
      onSuccess { user ->
        userDataState.value = Result.success(user)
      }
      onFailure {
        userDataState.value = Result.failure(it)
      }
    }
  }
  
  fun uploadImage(bitmap: Bitmap) {
    repository.uploadImage(bitmap) {
      onSuccess {
        uploadingImageState.value = RawResult.success()
      }
      onFailure {
        uploadingImageState.value = RawResult.failure(it)
      }
    }
  }
  
}
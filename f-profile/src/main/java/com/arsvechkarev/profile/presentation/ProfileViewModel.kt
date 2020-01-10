package com.arsvechkarev.profile.presentation

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arsvechkarev.profile.repositories.ProfileRepository
import core.RawResult
import core.model.users.User
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
  private val repository: ProfileRepository
) : ViewModel() {
  
  var userDataState = MutableLiveData<Result<User>>()
  var uploadingImageState = MutableLiveData<RawResult>()
  
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
  
  fun uploadImage(uri: Uri) {
    repository.uploadImage(uri) {
      onSuccess {
        uploadingImageState.value = RawResult.success()
      }
      
      onFailure {
        uploadingImageState.value = RawResult.failure(it)
      }
    }
  }
  
}
package com.arsvechkarev.profile.presentation

import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.profile.repositories.ProfileRepository
import core.base.BaseViewModel
import core.model.users.User
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
  private val repository: ProfileRepository
) : BaseViewModel() {
  
  var userData = MutableLiveData<Result<User>>()
  
  fun fetchProfileData() {
    repository.fetchProfileData {
      onSuccess { user ->
        userData.value = Result.success(user)
      }
      onFailure {
        userData.value = Result.failure(it)
      }
    }
  }
  
}
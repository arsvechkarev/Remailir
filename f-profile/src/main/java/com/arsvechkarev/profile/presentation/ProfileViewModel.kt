package com.arsvechkarev.profile.presentation

import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.profile.repositories.ProfileRepository
import core.base.BaseViewModel
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
  private val repository: ProfileRepository
) : BaseViewModel() {
  
  var userData = MutableLiveData<ProfileState>()
  
  fun fetchProfileData() {
    repository.fetchProfileData {
      onSuccess { user ->
        this@ProfileViewModel.userData.value = ProfileState.Success(user)
      }
      onFailure {
        userData.value = ProfileState.Failure(it)
      }
    }
  }
  
}
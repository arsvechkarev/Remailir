package com.arsvechkarev.profile.presentation

import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.core.base.BaseViewModel
import com.arsvechkarev.core.model.users.User
import com.arsvechkarev.profile.repositories.ProfileRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
  private val repository: ProfileRepository
) : BaseViewModel() {
  
  var userData = MutableLiveData<User>()
  
  fun fetchProfileData() {
    coroutine {
      repository.fetchProfileData {
        onSuccess { user ->
          this@ProfileViewModel.userData.value = user
        }
        onFailure {
        
        }
      }
    }
  }
  
}
package com.arsvechkarev.users.presentation

import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.users.repository.UsersRepository
import core.model.users.OtherUser
import firebase.FireViewModel
import javax.inject.Inject

class UsersViewModel @Inject constructor(
  private val repository: UsersRepository
) : FireViewModel<List<OtherUser>>(repository) {
  
  var otherUsers = MutableLiveData<Result<List<OtherUser>>>()
  
  fun fetchUsers() {
    repository.fetchUsers {
      onSuccess {
        otherUsers.value = Result.success(it)
      }
    
      onFailure {
        otherUsers.value = Result.failure(it)
      }
    }
  }
  
}
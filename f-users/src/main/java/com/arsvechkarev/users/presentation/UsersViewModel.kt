package com.arsvechkarev.users.presentation

import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.users.repository.UsersRepository
import core.MaybeResult
import core.RxJavaSchedulersProvider
import core.base.RxViewModel
import core.model.users.User
import javax.inject.Inject

class UsersViewModel @Inject constructor(
  private val repository: UsersRepository,
  private val schedulersProvider: RxJavaSchedulersProvider
) : RxViewModel() {
  
  var usersListData = MutableLiveData<MaybeResult<List<User>>>()
  
  fun fetchUsers() {
    rxCall {
      repository.fetchUsers()
        .subscribeOn(schedulersProvider.io)
        .observeOn(schedulersProvider.mainThread)
        .subscribe({
          if (it.isEmpty()) {
            usersListData.value = MaybeResult.nothing()
          } else {
            usersListData.value = MaybeResult.success(it)
          }
        }, {
          usersListData.value = MaybeResult.failure(it)
        })
    }
  }
  
}
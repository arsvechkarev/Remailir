package com.arsvechkarev.users.presentation

import androidx.lifecycle.LiveData
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
  
  private var _usersListData = MutableLiveData<MaybeResult<List<User>>>()
  private var _chatCreationState = MutableLiveData<Result<User>>()
  
  val usersListData: LiveData<MaybeResult<List<User>>> = _usersListData
  val chatCreationState: LiveData<Result<User>> = _chatCreationState
  
  fun createChat(otherUser: User) {
    rxCall {
      repository.createChat(otherUser)
        .subscribeOn(schedulersProvider.io)
        .observeOn(schedulersProvider.mainThread)
        .subscribe({
          _chatCreationState.value = Result.success(it)
        }, {
          _chatCreationState.value = Result.failure(it)
        })
    }
  }
  
  fun fetchUsers() {
    rxCall {
      repository.fetchUsers()
        .subscribeOn(schedulersProvider.io)
        .observeOn(schedulersProvider.mainThread)
        .subscribe({
          if (it.isEmpty()) {
            _usersListData.value = MaybeResult.nothing()
          } else {
            _usersListData.value = MaybeResult.success(it)
          }
        }, {
          _usersListData.value = MaybeResult.failure(it)
        })
    }
  }
  
}
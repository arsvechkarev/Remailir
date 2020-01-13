package com.arsvechkarev.messaging.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.messaging.list.toDisplayableItems
import com.arsvechkarev.messaging.repository.MessagingRepository
import core.MaybeResult
import core.RxJavaSchedulersProvider
import core.base.RxViewModel
import core.model.users.User
import core.recycler.DisplayableItem
import javax.inject.Inject

class MessagingViewModel @Inject constructor(
  private val repository: MessagingRepository,
  private val schedulersProvider: RxJavaSchedulersProvider
) : RxViewModel() {
  
  private val _messages = MutableLiveData<MaybeResult<List<DisplayableItem>>>()
  
  val messages: LiveData<MaybeResult<List<DisplayableItem>>> = _messages
  
  fun fetchMessagesList(otherUser: User) {
    rxCall {
      repository.fetchMessages(otherUser)
        .subscribeOn(schedulersProvider.io)
        .map { it.toDisplayableItems() }
        .observeOn(schedulersProvider.mainThread)
        .subscribe({
          _messages.value = MaybeResult.success(it)
        }, {
          _messages.value = MaybeResult.failure(it)
        }, {
          _messages.value = MaybeResult.nothing()
        })
    }
  }
  
  fun sendMessage(message: String) {
    repository.sendMessage(message)
  }
  
}
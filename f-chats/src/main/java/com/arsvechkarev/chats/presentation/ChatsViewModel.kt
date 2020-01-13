package com.arsvechkarev.chats.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.chats.repository.ChatsRepository
import core.RxJavaSchedulersProvider
import core.base.RxViewModel
import core.model.messaging.Chat
import log.log
import javax.inject.Inject

class ChatsViewModel @Inject constructor(
  private val chatsRepository: ChatsRepository,
  private val schedulersProvider: RxJavaSchedulersProvider
) : RxViewModel() {
  
  private val _chatsState = MutableLiveData<Result<List<Chat>>>()
  
  val chatState: LiveData<Result<List<Chat>>> = _chatsState
  
  fun fetchMessages() {
    rxCall {
      chatsRepository.fetchChats()
        .subscribeOn(schedulersProvider.io)
        .observeOn(schedulersProvider.mainThread)
        .subscribe({
          _chatsState.value = Result.success(it)
        }, {
          log(it) { "Messages fetch failed" }
          _chatsState.value = Result.failure(it)
        })
    }
  }
  
}
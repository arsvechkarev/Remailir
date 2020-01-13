package com.arsvechkarev.chats.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.chats.repository.ChatsRepository
import core.MaybeResult
import core.RxJavaSchedulersProvider
import core.base.RxViewModel
import core.model.messaging.Chat
import log.log
import javax.inject.Inject

class ChatsViewModel @Inject constructor(
  private val chatsRepository: ChatsRepository,
  private val schedulersProvider: RxJavaSchedulersProvider
) : RxViewModel() {
  
  private val _chatsState = MutableLiveData<MaybeResult<List<Chat>>>()
  
  val chatState: LiveData<MaybeResult<List<Chat>>> = _chatsState
  
  fun fetchMessages() {
    rxCall {
      chatsRepository.fetchChatsRx()
        .subscribeOn(schedulersProvider.io)
        .observeOn(schedulersProvider.mainThread)
        .subscribe({
          _chatsState.value = MaybeResult.success(it)
        }, {
          log(it) { "Messages fetch failed" }
          _chatsState.value = MaybeResult.failure(it)
        }, {
          log { "No chats found" }
          _chatsState.value = MaybeResult.nothing()
        })
    }
  }
  
}
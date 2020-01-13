package com.arsvechkarev.messaging.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.messaging.list.toDisplayableItems
import com.arsvechkarev.messaging.presentation.MessagingState.FailedToSent
import com.arsvechkarev.messaging.presentation.MessagingState.Success
import com.arsvechkarev.messaging.repository.MessagingRepository
import core.MaybeResult
import core.RxJavaSchedulersProvider
import core.base.RxViewModel
import core.extensions.currentTimestamp
import core.model.messaging.DialogMessage
import core.model.messaging.MessageOtherUser
import core.model.messaging.MessageThisUser
import core.model.users.User
import core.recycler.DisplayableItem
import log.Loggable
import log.log
import storage.AppUser
import javax.inject.Inject

class MessagingViewModel @Inject constructor(
  private val repository: MessagingRepository,
  private val schedulersProvider: RxJavaSchedulersProvider
) : RxViewModel(), Loggable {
  
  override val logTag = "Messaging_ViewModel"
  
  private lateinit var otherUser: User
  
  private val _messagesState = MutableLiveData<MaybeResult<List<DisplayableItem>>>()
  private val _sendingMessageState = MutableLiveData<MessagingState>()
  
  val messagesState: LiveData<MaybeResult<List<DisplayableItem>>> = _messagesState
  val sendingMessageState: LiveData<MessagingState> = _sendingMessageState
  
  fun fetchMessagesList(otherUser: User) {
    this.otherUser = otherUser
    rxCall {
      repository.fetchMessages(otherUser)
        .subscribeOn(schedulersProvider.io)
        .map { it.toDisplayableItems() }
        .doOnNext { messages ->
          messages.forEach {
            log { "=============" }
            when (it) {
              is MessageThisUser -> log { "thisUser -> ${it.text}" }
              is MessageOtherUser -> log { "otherUser -> ${it.text}" }
            }
          }
        }
        .observeOn(schedulersProvider.mainThread)
        .subscribe({
          _messagesState.value = MaybeResult.success(it)
        }, {
          _messagesState.value = MaybeResult.failure(it)
        }, {
          _messagesState.value = MaybeResult.nothing()
        })
    }
  }
  
  fun sendMessage(message: String) {
    log { "Sending message" }
    val dialogMessage = DialogMessage(AppUser.get().id, otherUser.id, message, currentTimestamp())
    rxCall {
      repository.sendMessage(dialogMessage)
        .subscribeOn(schedulersProvider.io)
        .observeOn(schedulersProvider.mainThread)
        .subscribe({
          log { "Sent message successfully" }
          _sendingMessageState.value = Success
        }, {
          _sendingMessageState.value = FailedToSent(it, dialogMessage)
        })
    }
  }
  
}
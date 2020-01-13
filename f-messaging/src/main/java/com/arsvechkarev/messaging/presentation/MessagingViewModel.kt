package com.arsvechkarev.messaging.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.messaging.presentation.MessagingState.MessagingList
import com.arsvechkarev.messaging.repository.MessagingRepository
import com.google.firebase.firestore.EventListener
import core.base.RxViewModel
import core.model.messaging.DialogMessage
import core.model.users.User
import javax.inject.Inject

class MessagingViewModel @Inject constructor(
  private var repository: MessagingRepository
) : RxViewModel() {
  
  private val _messages = MutableLiveData<MessagingState>()
  
  val messages: LiveData<MessagingState> = _messages
  
  fun fetchMessagesList(otherUser: User) {
    repository.fetchMessages(otherUser, EventListener { snapshot, exception ->
      if (exception != null) return@EventListener
      _messages.value = MessagingList(snapshot!!.toObjects(DialogMessage::class.java))
    })
  }
  
  fun sendMessage(message: String, otherUser: User) {
    repository.sendMessage(message, otherUser, onSuccess = {
    }, onFailure = {
    })
  }
  
}
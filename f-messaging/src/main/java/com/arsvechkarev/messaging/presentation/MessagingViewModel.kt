package com.arsvechkarev.messaging.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arsvechkarev.messaging.presentation.MessagingState.MessagingList
import com.arsvechkarev.messaging.repository.MessagingRepository
import com.google.firebase.firestore.EventListener
import core.model.messaging.DialogMessage
import core.model.users.User
import javax.inject.Inject

class MessagingViewModel @Inject constructor(
  private var repository: MessagingRepository
) : ViewModel() {
  
  val messages = MutableLiveData<MessagingState>()
  
  fun fetchMessagesList(otherUser: User) {
    repository.fetchMessages(otherUser, EventListener { snapshot, exception ->
      if (exception != null) return@EventListener
      messages.value = MessagingList(snapshot!!.toObjects(DialogMessage::class.java))
    })
  }
  
  fun sendMessage(message: String, otherUser: User) {
    repository.sendMessage(message, otherUser, onSuccess = {
    }, onFailure = {
    })
  }
  
}
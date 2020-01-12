package com.arsvechkarev.chat.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arsvechkarev.chat.presentation.MessagesState.MessagesList
import com.arsvechkarev.chat.repository.ChatRepository
import com.google.firebase.firestore.EventListener
import core.model.messaging.DialogMessage
import core.model.users.User
import javax.inject.Inject

class ChatViewModel @Inject constructor(
  private var repository: ChatRepository
) : ViewModel() {
  
  val messages = MutableLiveData<MessagesState>()
  
  fun fetchMessagesList(otherUser: User) {
    repository.fetchMessages(otherUser, EventListener { snapshot, exception ->
      if (exception != null) return@EventListener
      messages.value = MessagesList(snapshot!!.toObjects(DialogMessage::class.java))
    })
  }
  
  fun sendMessage(message: String, otherUser: User) {
    repository.sendMessage(message, otherUser, onSuccess = {
    }, onFailure = {
    })
  }
  
}
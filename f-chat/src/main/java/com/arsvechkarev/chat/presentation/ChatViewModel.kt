package com.arsvechkarev.chat.presentation

import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.chat.presentation.MessagesState.MessagesList
import com.arsvechkarev.chat.repository.ChatRepository
import core.base.FireViewModel
import core.model.messaging.DialogMessage
import core.model.users.OtherUser
import com.google.firebase.firestore.EventListener
import javax.inject.Inject

class ChatViewModel @Inject constructor(
  private var repository: ChatRepository
) : FireViewModel() {
  
  val messages = MutableLiveData<MessagesState>()
  
  fun fetchMessagesList(otherUser: OtherUser) {
    repository.fetchMessages(otherUser, EventListener { snapshot, exception ->
      if (exception != null) return@EventListener
      messages.value = MessagesList(snapshot!!.toObjects(DialogMessage::class.java))
    })
  }

  fun sendMessage(message: String, otherUser: OtherUser) {
    repository.sendMessage(message, otherUser, onSuccess = {
    }, onFailure = {
    })
  }
  
}
package com.arsvechkarev.chat.presentation

import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.chat.presentation.MessagesState.*
import com.arsvechkarev.chat.repository.ChatRepository
import com.arsvechkarev.core.base.FireViewModel
import com.arsvechkarev.core.model.OtherUser
import com.arsvechkarev.core.model.messaging.DialogMessage
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
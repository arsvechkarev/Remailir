package com.arsvechkarev.chat.presentation

import core.model.messaging.DialogMessage

sealed class MessagesState {
  
  class SingleMessage(val message: DialogMessage) : MessagesState()
  class MessagesList(val messages: List<DialogMessage>) : MessagesState()
  class Failure(val error: Throwable) : MessagesState()
}
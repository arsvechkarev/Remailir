package com.arsvechkarev.messaging.presentation

import core.model.messaging.DialogMessage

sealed class MessagingState {
  
  class SingleMessage(val message: DialogMessage) : MessagingState()
  class MessagingList(val messages: List<DialogMessage>) : MessagingState()
  class Failure(val error: Throwable) : MessagingState()
}
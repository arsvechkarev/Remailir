package com.arsvechkarev.messaging.presentation

import core.model.messaging.DialogMessage

sealed class MessagingState {
  
  class FailedToSent(val error: Throwable, val message: DialogMessage) : MessagingState()
  
  object Success : MessagingState()
}
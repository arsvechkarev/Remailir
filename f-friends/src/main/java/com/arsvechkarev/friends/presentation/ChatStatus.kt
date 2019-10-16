package com.arsvechkarev.friends.presentation

import com.arsvechkarev.core.model.Friend

sealed class ChatStatus {
  
  class Success(val friend: Friend): ChatStatus()
  
  class Failure(val error: Throwable) : ChatStatus()
}
package com.arsvechkarev.chat.domain

import com.arsvechkarev.core.model.messaging.MessageOtherUser
import com.arsvechkarev.core.model.messaging.MessageThisUser

interface ChattingObserver {
  
  fun showOtherUserJoined()
  
  fun showOtherUserBecameInactive()
  
  fun showOtherUserLeftChatting()
  
  fun showMessageSent(message: MessageThisUser)
  
  fun showMessageReceived(message: MessageOtherUser)
  
}
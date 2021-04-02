package com.arsvechkarev.chat.domain

import core.model.messaging.MessageOtherUser
import core.model.messaging.MessageThisUser

interface ChattingObserver {
  
  fun showOtherUserJoined()
  
  fun showOtherUserBecameInactive()
  
  fun showOtherUserLeftChatting()
  
  fun showMessageSent(message: MessageThisUser)
  
  fun showMessageReceived(message: MessageOtherUser)
  
}
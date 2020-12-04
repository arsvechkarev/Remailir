package com.arsvechkarev.firebase.firestore.chatmanaging

interface ChatWaitingDataSource {
  
  suspend fun setCurrentUserAsActive(otherUserUsername: String)
  
  suspend fun waitForJoining(otherUserUsername: String, listener: ChatWaitingListener)
  
  fun releaseJoiningListener()
}
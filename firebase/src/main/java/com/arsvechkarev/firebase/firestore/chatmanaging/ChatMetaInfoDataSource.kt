package com.arsvechkarev.firebase.firestore.chatmanaging

interface ChatMetaInfoDataSource {
  
  suspend fun waitForJoining(otherUserUsername: String, listener: ChatWaitingListener)
  
  suspend fun setCurrentUserAsActive(otherUserUsername: String)
  
  fun setCurrentUserAsInactive(otherUserUsername: String)
  
  fun releaseJoiningListener()
}
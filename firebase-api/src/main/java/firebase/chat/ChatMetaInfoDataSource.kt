package firebase.chat

interface ChatMetaInfoDataSource {
  
  suspend fun waitForJoining(otherUserUsername: String, listener: ChatWaitingListener)
  
  suspend fun setCurrentUserAsActive(otherUserUsername: String)
  
  suspend fun exitChat(otherUserUsername: String)
  
  fun releaseJoiningListener()
}
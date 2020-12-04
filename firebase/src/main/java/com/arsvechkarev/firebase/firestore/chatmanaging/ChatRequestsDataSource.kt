package com.arsvechkarev.firebase.firestore.chatmanaging

interface ChatRequestsDataSource {
  
  /**
   * Returns list of users currently waiting to chat
   */
  suspend fun getCurrentlyWaitingForChat(): List<String>
  
  /**
   * Respond to chat request. Returns true if responded successfully, false
   * if other user already cancelled request
   */
  suspend fun respondToChatRequest(otherUserUsername: String): Boolean
}
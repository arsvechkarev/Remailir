package com.arsvechkarev.home.domain

import com.arsvechkarev.core.model.User
import com.arsvechkarev.core.model.toUsersList
import com.arsvechkarev.firebase.firestore.waiting.ChatRequestsDataSource

class HomeRepository(
  private val chatDatabase: ChatRequestsDataSource
) {
  
  suspend fun getUsersWaitingToChat(): List<User> {
    return chatDatabase.getCurrentlyWaitingForChat().toUsersList()
  }
  
  suspend fun respondToChatRequest(user: User): Boolean {
    return chatDatabase.respondToChatRequest(user.username)
  }
}
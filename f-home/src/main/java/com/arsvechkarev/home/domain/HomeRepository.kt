package com.arsvechkarev.home.domain

import firebase.chat.ChatRequestsDataSource
import core.model.User
import core.model.toUsersList

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
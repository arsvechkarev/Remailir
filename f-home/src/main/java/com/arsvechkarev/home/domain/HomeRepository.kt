package com.arsvechkarev.home.domain

import core.StringToUserMapper
import core.di.FeatureScope
import core.model.User
import firebase.chat.ChatsRequestsDataSource
import javax.inject.Inject

@FeatureScope
class HomeRepository @Inject constructor(
  private val chatsDatasource: ChatsRequestsDataSource,
  private val mapper: StringToUserMapper
) {
  
  suspend fun getUsersWaitingToChat(): List<User> {
    return chatsDatasource.getCurrentlyWaitingForChat().map(mapper::map)
  }
  
  suspend fun respondToChatRequest(user: User): Boolean {
    return chatsDatasource.respondToChatRequest(user.username)
  }
}
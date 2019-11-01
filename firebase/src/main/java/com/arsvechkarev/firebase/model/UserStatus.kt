package com.arsvechkarev.firebase.model

class UserStatus(
  val status: String,
  val lastSeen: Long,
  val isTyping: Boolean
)

enum class OnlineOfflineStatus {
  ONLINE, OFFLINE
}

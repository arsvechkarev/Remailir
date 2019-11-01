package com.arsvechkarev.firebase

import com.arsvechkarev.core.strings.UNDERSCORE
import com.arsvechkarev.firebase.model.OnlineOfflineStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.UUID

fun randomUid(): String = UUID.randomUUID().toString()

val thisUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

fun defineStatus(onlineOfflineStatus: OnlineOfflineStatus): String {
  return when (onlineOfflineStatus) {
    OnlineOfflineStatus.ONLINE -> "online"
    OnlineOfflineStatus.OFFLINE -> "offline"
  }
}

fun getChatIdWith(otherUserId: String): String {
  val thisUserId = FirebaseAuth.getInstance().uid!!
  return if (otherUserId < thisUserId) {
    otherUserId + UNDERSCORE + thisUserId
  } else {
    thisUserId + UNDERSCORE + otherUserId
  }
}

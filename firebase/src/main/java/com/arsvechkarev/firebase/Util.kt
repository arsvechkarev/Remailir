package com.arsvechkarev.firebase

import com.arsvechkarev.core.model.users.OtherUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

fun defineStatus(onlineOfflineStatus: OnlineOfflineStatus): String {
  return when (onlineOfflineStatus) {
    OnlineOfflineStatus.ONLINE -> "online"
    OnlineOfflineStatus.OFFLINE -> "offline"
  }
}

fun getChatIdWith(otherUser: OtherUser) = getChatIdWith(otherUser.id)

fun getChatIdWith(otherUserId: String): String {
  val thisUserId = FirebaseAuth.getInstance().uid!!
  return organizeIdsForChat(otherUserId, thisUserId)
}

fun organizeIdsForChat(id1: String, id2: String): String {
  return if (id1 < id2) id1 + UNDERSCORE + id2 else id2 + UNDERSCORE + id1
}

val thisUserId: String get() = FirebaseAuth.getInstance().uid!!

val thisUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
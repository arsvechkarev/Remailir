package com.arsvechkarev.firebase.firestore

object FirestoreSchema {
  
  const val chats = "chats"
  const val messages = "messages"
  const val active_prefix = "active_"
  const val participants = "participants"
  
  fun Any.chatDocumentName(thisUsername: String, otherUsername: String): String {
    return "${minOf(thisUsername, otherUsername)}-${maxOf(thisUsername, otherUsername)}"
  }
  
  fun Any.activeUserFieldName(username: String): String {
    return "$active_prefix$username"
  }
}
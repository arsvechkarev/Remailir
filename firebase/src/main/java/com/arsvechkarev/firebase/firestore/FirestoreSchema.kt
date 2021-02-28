package com.arsvechkarev.firebase.firestore

object FirestoreSchema {
  
  const val chats = "chats"
  const val messages = "messages"
  const val participants = "participants"
  const val status_prefix = "status_"
  const val status_not_chatting = "not_chatting"
  const val status_chatting = "chatting"
  const val status_waiting = "waiting"
  const val waiting_prefix = "waiting_"
  const val chatting_prefix = "chatting_"
  
  fun chatDocumentName(thisUsername: String, otherUsername: String): String {
    return "${minOf(thisUsername, otherUsername)}-${maxOf(thisUsername, otherUsername)}"
  }
  
  fun statusFieldName(username: String): String {
    return "$status_prefix$username"
  }
  
  fun waitingUserFieldName(username: String): String {
    return "$waiting_prefix$username"
  }
  
  fun chattingUserFieldName(username: String): String {
    return "$chatting_prefix$username"
  }
}
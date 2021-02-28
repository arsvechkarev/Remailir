package com.arsvechkarev.firebase.firestore.chat

interface ChatWaitingListener {
  
  fun onUserBecameActive()
  
  fun onUserBecameInactive()
}
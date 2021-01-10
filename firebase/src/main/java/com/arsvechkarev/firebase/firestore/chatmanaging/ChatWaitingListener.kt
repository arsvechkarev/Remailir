package com.arsvechkarev.firebase.firestore.chatmanaging

interface ChatWaitingListener {
  
  fun onUserBecameActive()
  
  fun onUserBecameInactive()
}
package com.arsvechkarev.firebase.firestore.chatmanaging

interface ChatWaitingListener {
  
  fun onUserJoined()
  
  fun onUserCancelledRequest()
}
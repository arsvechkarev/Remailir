package com.arsvechkarev.firebase.firestore.waiting

interface ChatWaitingListener {
  
  fun onUserJoined()
  
  fun onUserCancelledRequest()
}
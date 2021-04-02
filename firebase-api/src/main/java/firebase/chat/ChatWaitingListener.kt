package firebase.chat

interface ChatWaitingListener {
  
  fun onUserBecameActive()
  
  fun onUserBecameInactive()
}
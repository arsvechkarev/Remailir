package com.arsvechkarev.chat.domain

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

class ChatExitingService : Service() {
  
  //  private val chatFirebaseDataSource = ChatFirebaseDataSource(
  //    FirebaseAuthenticator.getUsername(),
  //    CoreComponent.instance.dispatchers()
  //  )
  
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    require(intent != null)
    val otherUserUsername = intent.getStringExtra(OTHER_USER_USERNAME)!!
    exitChat(otherUserUsername)
    return START_REDELIVER_INTENT
  }
  
  private fun exitChat(otherUserUsername: String) {
    thread {
      // TODO (4/1/2021): Handle threading properly
      runBlocking {
        //        chatFirebaseDataSource.exitChat(otherUserUsername)
      }
    }
  }
  
  override fun onBind(intent: Intent?): IBinder? {
    return null
  }
  
  companion object {
    
    const val OTHER_USER_USERNAME = "OTHER_USER_USERNAME"
  }
}
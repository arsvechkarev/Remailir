package com.arsvechkarev.chat.domain

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.arsvechkarev.core.concurrency.AndroidDispatchers
import com.arsvechkarev.core.concurrency.AndroidThreader
import com.arsvechkarev.core.concurrency.Threader
import com.arsvechkarev.core.extenstions.assertThat
import com.arsvechkarev.firebase.auth.FirebaseAuthenticator
import com.arsvechkarev.firebase.firestore.chat.ChatFirebaseDataSource
import kotlinx.coroutines.runBlocking

class ChatExitingService : Service() {
  
  private val chatFirebaseDataSource = ChatFirebaseDataSource(
    FirebaseAuthenticator.getUsername(),
    AndroidDispatchers
  )
  
  private val threader: Threader = AndroidThreader
  
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    assertThat(intent != null)
    val otherUserUsername = intent.getStringExtra(OTHER_USER_USERNAME)!!
    exitChat(otherUserUsername)
    return START_REDELIVER_INTENT
  }
  
  private fun exitChat(otherUserUsername: String) {
    threader.onIoThread {
      runBlocking {
        chatFirebaseDataSource.exitChat(otherUserUsername)
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
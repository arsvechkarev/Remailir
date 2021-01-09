package com.arsvechkarev.chat.domain

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class ChattingService : Service() {
  
  private val binder = ChattingBinder()
  
  override fun onBind(intent: Intent): IBinder {
    return binder
  }
  
  inner class ChattingBinder : Binder() {
    
    fun getService(): ChattingService = this@ChattingService
  }
}
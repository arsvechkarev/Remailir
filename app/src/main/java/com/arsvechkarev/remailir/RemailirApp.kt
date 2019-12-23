package com.arsvechkarev.remailir

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import log.Logger

class RemailirApp : Application() {
  
  override fun onCreate() {
    super.onCreate()
    Logger.activate()
    AndroidThreeTen.init(this)
  }
}
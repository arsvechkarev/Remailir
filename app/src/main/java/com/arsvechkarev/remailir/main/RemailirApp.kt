package com.arsvechkarev.remailir.main

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber

class RemailirApp : Application() {
  
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    AndroidThreeTen.init(this)
  }
}
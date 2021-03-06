package com.arsvechkarev.remailir

import android.app.Application
import com.arsvechkarev.core.viewbuilding.Fonts
import com.arsvechkarev.viewdsl.ContextHolder
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber

class RemailirApplication : Application() {
  
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    ContextHolder.init(applicationContext)
    Fonts.init(applicationContext)
    FirebaseDatabase.getInstance().setPersistenceEnabled(false)
  }
}
package com.arsvechkarev.remailir

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import core.di.CoreComponent
import core.di.CoreComponentProvider
import core.di.DaggerCoreComponent
import log.Logger

class RemailirApp : Application(), CoreComponentProvider {
  
  override fun onCreate() {
    super.onCreate()
    Logger.activate()
    AndroidThreeTen.init(this)
  }
  
  override val coreComponent: CoreComponent
    get() = DaggerCoreComponent.create()
}
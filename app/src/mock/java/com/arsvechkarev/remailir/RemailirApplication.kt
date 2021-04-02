package com.arsvechkarev.remailir

import com.arsvechkarev.remailir.di.AppComponent
import com.arsvechkarev.remailir.fakes.MockModeProviders
import core.MockModeDrawerHolder
import core.di.AppDependenciesProvider
import timber.log.Timber

class RemailirApplication : BaseRemailirApplication() {
  
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    MockModeDrawerHolder.setMockModeDrawer(MockModeDrawerImpl)
  }
  
  override fun configureDependencies() {
    AppDependenciesProvider.initialize(AppComponent.createComponent(MockModeProviders(this)))
  }
}
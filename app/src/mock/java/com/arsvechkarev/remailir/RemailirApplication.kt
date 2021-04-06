package com.arsvechkarev.remailir

import com.arsvechkarev.remailir.di.AppComponent
import com.arsvechkarev.remailir.fakes.MockModeProviders
import config.NetworkConfigurator
import core.MockModeDrawerHolder
import core.di.AppDependenciesProvider
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class RemailirApplication : BaseRemailirApplication() {
  
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    MockModeDrawerHolder.setMockModeDrawer(MockModeDrawerImpl)
    NetworkConfigurator.configureFakeNetworkDelay(1000)
    runBlocking {
      val storage = AppDependenciesProvider.instance.provideThisUserStorage()
      storage.saveUsername("a")
      storage.saveEmail("a@gmail.com")
    }
  }
  
  override fun configureDependencies() {
    AppDependenciesProvider.initialize(AppComponent.createComponent(MockModeProviders(this)))
  }
}
package com.arsvechkarev.remailir.di

import authentication.AuthDependenciesProvider
import core.di.AppDependenciesProvider
import core.di.CoreDependenciesProvider
import core.di.ServiceStarterProvider
import core.di.SettingsProvider
import dagger.Component
import firebase.FirebaseDependenciesProvider
import javax.inject.Singleton

@Singleton
@Component(
  dependencies = [
    CoreDependenciesProvider::class,
    AuthDependenciesProvider::class,
    FirebaseDependenciesProvider::class,
    SettingsProvider::class,
    ServiceStarterProvider::class
  ]
)
interface AppComponent : AppDependenciesProvider {
  
  companion object {
    
    fun createComponent(providers: Providers): AppComponent {
      return DaggerAppComponent.builder()
          .coreDependenciesProvider(providers.coreDependenciesProvider)
          .settingsProvider(providers.settingsProvider)
          .serviceStarterProvider(providers.serviceStarterProvider)
          .authDependenciesProvider(providers.authDependenciesProvider)
          .firebaseDependenciesProvider(providers.firebaseDependenciesProvider)
          .build()
    }
  }
}
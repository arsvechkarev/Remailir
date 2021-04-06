package com.arsvechkarev.remailir.di

import core.di.AuthDependenciesProvider
import core.di.AppDependenciesProvider
import core.di.CoreDependenciesProvider
import core.di.ServiceStarterProvider
import core.di.SettingsProvider
import dagger.Component
import firebase.chat.FirebaseChatRequestsDataSourceProvider
import firebase.database.FirebaseDatabaseDependenciesProvider
import javax.inject.Singleton

@Singleton
@Component(
  dependencies = [
    CoreDependenciesProvider::class,
    AuthDependenciesProvider::class,
    FirebaseDatabaseDependenciesProvider::class,
    FirebaseChatRequestsDataSourceProvider::class,
    SettingsProvider::class,
    ServiceStarterProvider::class
  ]
)
interface AppComponent : AppDependenciesProvider {
  
  companion object {
    
    fun createComponent(providers: Providers): AppComponent {
      return DaggerAppComponent.builder()
          .coreDependenciesProvider(providers.coreDependenciesProvider)
          .authDependenciesProvider(providers.authDependenciesProvider)
          .firebaseDatabaseDependenciesProvider(providers.firebaseDatabaseDependenciesProvider)
          .firebaseChatRequestsDataSourceProvider(providers.firebaseChatRequestsDataSourceProvider)
          .settingsProvider(providers.settingsProvider)
          .serviceStarterProvider(providers.serviceStarterProvider)
          .build()
    }
  }
}
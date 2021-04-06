package com.arsvechkarev.remailir.di

import android.app.Application
import core.impl.di.AuthDependenciesComponent
import core.impl.di.CoreDependenciesComponent
import core.impl.di.ServiceStarterComponent
import core.impl.di.SettingsComponent
import firebase.chat.FirebaseChatRequestsDataSourceProvider
import firebase.impl.di.FirebaseChatsComponent
import firebase.impl.di.FirebaseDatabaseComponent
import navigation.RouterProvider

class DefaultProviders(app: Application) : Providers {
  
  override val coreDependenciesProvider = CoreDependenciesComponent.createComponent(app)
  override val routerProvider: RouterProvider = RouterComponent.createComponent()
  override val settingsProvider = SettingsComponent.createComponent(app)
  override val serviceStarterProvider = ServiceStarterComponent.createComponent(app)
  override val authDependenciesProvider = AuthDependenciesComponent.createComponent()
  override val firebaseDatabaseDependenciesProvider = FirebaseDatabaseComponent.createComponent(
    coreDependenciesProvider)
  override val firebaseChatRequestsDataSourceProvider: FirebaseChatRequestsDataSourceProvider =
      FirebaseChatsComponent.createComponent(coreDependenciesProvider)
}
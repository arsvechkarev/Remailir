package com.arsvechkarev.remailir.di

import android.app.Application
import authentication.AuthDependenciesProvider
import authentication.impl.di.AuthDependenciesComponent
import core.impl.di.CoreDependenciesComponent
import core.impl.di.ServiceStarterComponent
import core.impl.di.SettingsComponent
import firebase.FirebaseDependenciesProvider
import firebase.impl.FirebaseComponent

class DefaultProviders(app: Application) : Providers {
  
  override val coreDependenciesProvider = CoreDependenciesComponent.createComponent()
  override val settingsProvider = SettingsComponent.createComponent(app)
  override val serviceStarterProvider = ServiceStarterComponent.createComponent(app)
  override val authDependenciesProvider = AuthDependenciesComponent.createComponent(app)
  override val firebaseDependenciesProvider = FirebaseComponent.createComponent(
    coreDependenciesProvider)
  
  init {
    AuthDependenciesProvider.initialize(authDependenciesProvider)
    FirebaseDependenciesProvider.initialize(firebaseDependenciesProvider)
  }
}
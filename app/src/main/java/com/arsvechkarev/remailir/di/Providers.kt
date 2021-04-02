package com.arsvechkarev.remailir.di

import authentication.AuthDependenciesProvider
import core.di.CoreDependenciesProvider
import core.di.ServiceStarterProvider
import core.di.SettingsProvider
import firebase.FirebaseDependenciesProvider

interface Providers {
  
  val coreDependenciesProvider: CoreDependenciesProvider
  
  val settingsProvider: SettingsProvider
  
  val serviceStarterProvider: ServiceStarterProvider
  
  val authDependenciesProvider: AuthDependenciesProvider
  
  val firebaseDependenciesProvider: FirebaseDependenciesProvider
}
package com.arsvechkarev.remailir.di

import core.di.AuthDependenciesProvider
import core.di.CoreDependenciesProvider
import core.di.ServiceStarterProvider
import core.di.SettingsProvider
import firebase.chat.FirebaseChatRequestsDataSourceProvider
import firebase.database.FirebaseDatabaseDependenciesProvider
import navigation.RouterProvider

interface Providers {
  
  val coreDependenciesProvider: CoreDependenciesProvider
  
  val routerProvider: RouterProvider
  
  val authDependenciesProvider: AuthDependenciesProvider
  
  val firebaseDatabaseDependenciesProvider: FirebaseDatabaseDependenciesProvider
  
  val firebaseChatRequestsDataSourceProvider: FirebaseChatRequestsDataSourceProvider
  
  val settingsProvider: SettingsProvider
  
  val serviceStarterProvider: ServiceStarterProvider
}
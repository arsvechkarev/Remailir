package com.arsvechkarev.remailir.fakes

import android.app.Application
import authentication.AuthDependenciesProvider
import authentication.impl.di.AuthDependenciesComponent
import com.arsvechkarev.remailir.di.Providers
import core.impl.di.CoreDependenciesComponent
import core.impl.di.ServiceStarterComponent
import core.impl.di.SettingsComponent
import firebase.FirebaseDependenciesProvider
import firebase.database.ByUsernameUsersActions
import firebase.database.FirebaseDatabase
import firebase.database.UsersActions
import firebase.database.UsersDatabaseSchema
import firebase.impl.PathDatabaseSchema

class MockModeProviders(app: Application) : Providers {
  
  override val coreDependenciesProvider = CoreDependenciesComponent.createComponent()
  override val settingsProvider = SettingsComponent.createComponent(app)
  override val serviceStarterProvider = ServiceStarterComponent.createComponent(app)
  override val authDependenciesProvider = AuthDependenciesComponent.createComponent(app)
  
  override val firebaseDependenciesProvider = object : FirebaseDependenciesProvider {
    
    private val database = FakeFirebaseDatabase(app)
    
    override fun provideUsersDatabaseSchema(): UsersDatabaseSchema {
      return PathDatabaseSchema
    }
    
    override fun provideFirebaseDatabase(): FirebaseDatabase {
      return database
    }
    
    override fun provideUserActions(): UsersActions {
      return ByUsernameUsersActions
    }
  }
  
  init {
    AuthDependenciesProvider.initialize(authDependenciesProvider)
    FirebaseDependenciesProvider.initialize(firebaseDependenciesProvider)
  }
}
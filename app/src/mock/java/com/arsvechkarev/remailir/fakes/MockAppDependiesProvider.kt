package com.arsvechkarev.remailir.fakes

import android.app.Application
import com.arsvechkarev.remailir.di.Providers
import com.arsvechkarev.remailir.di.RouterComponent
import core.di.AuthDependenciesProvider
import core.impl.di.CoreDependenciesComponent
import core.impl.di.ServiceStarterComponent
import core.impl.di.SettingsComponent
import firebase.chat.FirebaseChatRequestsDataSourceProvider
import firebase.database.ByUsernameUsersActions
import firebase.database.FirebaseDatabase
import firebase.database.FirebaseDatabaseDependenciesProvider
import firebase.database.UsersActions
import firebase.database.UsersDatabaseSchema
import firebase.impl.PathDatabaseSchema
import firebase.impl.di.FirebaseChatsComponent
import navigation.RouterProvider

class MockModeProviders(app: Application) : Providers {
  
  override val coreDependenciesProvider = CoreDependenciesComponent.createComponent(app)
  override val routerProvider: RouterProvider = RouterComponent.createComponent()
  override val settingsProvider = SettingsComponent.createComponent(app)
  override val serviceStarterProvider = ServiceStarterComponent.createComponent(app)
  
  override val authDependenciesProvider = object : AuthDependenciesProvider {
  
    init {
      AuthDependenciesProvider.initialize(this)
    }
    
    override fun provideAuthenticator() = FakeAuthenticator
  }
  
  override val firebaseDatabaseDependenciesProvider = object :
    FirebaseDatabaseDependenciesProvider {
    
    init {
      FirebaseDatabaseDependenciesProvider.initialize(this)
    }
    
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
  
  override val firebaseChatRequestsDataSourceProvider: FirebaseChatRequestsDataSourceProvider =
      FirebaseChatsComponent.createComponent(coreDependenciesProvider)
}
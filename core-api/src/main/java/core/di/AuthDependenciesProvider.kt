package core.di

import core.Authenticator

interface AuthDependenciesProvider {
  
  fun provideAuthenticator(): Authenticator
  
  companion object {
    
    private var _instance: AuthDependenciesProvider? = null
    val instance: AuthDependenciesProvider
      get() = _instance ?: error("You should call initialize() first")
    
    fun initialize(authDependenciesProvider: AuthDependenciesProvider) {
      _instance = authDependenciesProvider
    }
  }
}
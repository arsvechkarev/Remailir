package firebase

import firebase.database.FirebaseDatabase
import firebase.database.UsersActions
import firebase.database.UsersDatabaseSchema

interface FirebaseDependenciesProvider {
  
  fun provideUsersDatabaseSchema(): UsersDatabaseSchema
  
  fun provideFirebaseDatabase(): FirebaseDatabase
  
  fun provideUserActions(): UsersActions
  
  companion object {
    
    private var _instance: FirebaseDependenciesProvider? = null
    val instance: FirebaseDependenciesProvider
      get() = _instance ?: error("You should call initialize() first")
    
    fun initialize(firebaseDependenciesProvider: FirebaseDependenciesProvider) {
      _instance = firebaseDependenciesProvider
    }
  }
}
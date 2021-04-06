package firebase.database

interface FirebaseDatabaseDependenciesProvider {
  
  fun provideUsersDatabaseSchema(): UsersDatabaseSchema
  
  fun provideFirebaseDatabase(): FirebaseDatabase
  
  fun provideUserActions(): UsersActions
  
  companion object {
    
    private var _instance: FirebaseDatabaseDependenciesProvider? = null
    val instance: FirebaseDatabaseDependenciesProvider
      get() = _instance ?: error("You should call initialize() first")
    
    fun initialize(firebaseDatabaseDependenciesProvider: FirebaseDatabaseDependenciesProvider) {
      _instance = firebaseDatabaseDependenciesProvider
    }
  }
}
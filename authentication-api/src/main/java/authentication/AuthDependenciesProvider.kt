package authentication

interface AuthDependenciesProvider {
  
  fun provideAuthenticator(): Authenticator
  
  fun provideEmailSaver(): EmailSaver
  
  companion object {
    
    private var _instance: AuthDependenciesProvider? = null
    val instance: AuthDependenciesProvider
      get() = _instance ?: error("You should call initialize() first")
    
    fun initialize(authDependenciesProvider: AuthDependenciesProvider) {
      _instance = authDependenciesProvider
    }
  }
}
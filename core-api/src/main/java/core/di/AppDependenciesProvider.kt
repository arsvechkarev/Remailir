package core.di

interface AppDependenciesProvider :
  CoreDependenciesProvider,
  SettingsProvider,
  ServiceStarterProvider {
  
  companion object {
    
    private var _instance: AppDependenciesProvider? = null
    val instance: AppDependenciesProvider
      get() = _instance ?: error("You should call initialize() first")
    
    fun initialize(appDependenciesProvider: AppDependenciesProvider) {
      _instance = appDependenciesProvider
    }
  }
}
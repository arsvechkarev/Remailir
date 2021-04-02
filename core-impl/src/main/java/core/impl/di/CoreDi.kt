package core.impl.di

import core.Dispatchers
import core.di.CoreDependenciesProvider
import core.impl.AndroidDispatchers
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [CoreModule::class])
interface CoreDependenciesComponent : CoreDependenciesProvider {
  
  companion object {
    
    fun createComponent(): CoreDependenciesComponent = DaggerCoreDependenciesComponent.create()
  }
}

@Module
object CoreModule {
  
  @JvmStatic
  @Provides
  @Singleton
  fun provideDispatchers(): Dispatchers = AndroidDispatchers
}
package core.impl.di

import android.app.Application
import core.Dispatchers
import core.ThisUserInfoProvider
import core.ThisUserInfoStorage
import core.di.CoreDependenciesProvider
import core.impl.AndroidDispatchers
import core.impl.ThisUserInfoStorageImpl
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [CoreModule::class])
interface CoreDependenciesComponent : CoreDependenciesProvider {
  
  @Component.Builder
  interface Builder {
    
    @BindsInstance
    fun app(app: Application): Builder
    
    fun build(): CoreDependenciesComponent
  }
  
  companion object {
    
    fun createComponent(app: Application): CoreDependenciesComponent {
      return DaggerCoreDependenciesComponent.builder()
          .app(app)
          .build()
    }
  }
}

@Module
object CoreModule {
  
  @Provides
  @Singleton
  @JvmStatic
  fun provideDispatchers(): Dispatchers = AndroidDispatchers
  
  
  @Provides
  @Singleton
  @JvmStatic
  fun provideThisUserInfoProvider(app: Application, dispatchers: Dispatchers): ThisUserInfoProvider {
    return provideThisUserInfoStorage(app, dispatchers)
  }
  
  @Provides
  @Singleton
  @JvmStatic
  fun provideThisUserInfoStorage(app: Application, dispatchers: Dispatchers): ThisUserInfoStorage {
    return ThisUserInfoStorageImpl(app, dispatchers)
  }
}
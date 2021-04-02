package core.impl.di

import android.app.Application
import core.ServiceStarter
import core.di.ServiceStarterProvider
import core.impl.ServiceStarterImpl
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [ServiceStarterModule::class])
interface ServiceStarterComponent : ServiceStarterProvider {
  
  @Component.Builder
  interface Builder {
    
    @BindsInstance
    fun app(app: Application): Builder
    
    fun build(): ServiceStarterComponent
  }
  
  companion object {
    
    fun createComponent(app: Application): ServiceStarterComponent {
      return DaggerServiceStarterComponent.builder().app(app).build()
    }
  }
}

@Module
class ServiceStarterModule {
  
  @Provides
  @Singleton
  fun provideServiceStarter(app: Application): ServiceStarter = ServiceStarterImpl(app)
}
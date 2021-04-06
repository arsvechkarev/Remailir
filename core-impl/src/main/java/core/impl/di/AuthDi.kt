package core.impl.di

import core.Authenticator
import core.di.AuthDependenciesProvider
import core.impl.FirebaseAuthenticator
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Component(modules = [AuthModule::class])
@Singleton
interface AuthDependenciesComponent : AuthDependenciesProvider {
  
  companion object {
    
    fun createComponent(): AuthDependenciesComponent {
      val component = DaggerAuthDependenciesComponent.create()
      AuthDependenciesProvider.initialize(component)
      return component
    }
  }
}

@Module
object AuthModule {
  
  @Provides
  @Singleton
  @JvmStatic
  fun provideAuthenticator(): Authenticator = FirebaseAuthenticator
}
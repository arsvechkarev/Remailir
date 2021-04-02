package authentication.impl.di

import android.app.Application
import authentication.AuthDependenciesProvider
import authentication.Authenticator
import authentication.EmailSaver
import authentication.impl.FirebaseAuthenticator
import authentication.impl.SharedPrefsEmailSaver
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Component(modules = [AuthModule::class])
@Singleton
interface AuthDependenciesComponent : AuthDependenciesProvider {
  
  @Component.Builder
  interface Builder {
    
    @BindsInstance
    fun app(app: Application): Builder
    
    fun build(): AuthDependenciesComponent
  }
  
  companion object {
    
    fun createComponent(app: Application): AuthDependenciesComponent {
      return DaggerAuthDependenciesComponent.builder()
          .app(app)
          .build()
    }
  }
}

@Module
object AuthModule {
  
  @Provides
  @Singleton
  @JvmStatic
  fun provideAuthenticator(): Authenticator = FirebaseAuthenticator
  
  @Provides
  @Singleton
  @JvmStatic
  fun provideEmailSaver(app: Application): EmailSaver = SharedPrefsEmailSaver(app)
}
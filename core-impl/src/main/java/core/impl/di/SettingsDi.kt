package core.impl.di

import android.app.Application
import core.Settings
import core.di.SettingsProvider
import core.impl.AndroidSettings
import core.impl.SharedPrefsStorage
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [SettingsModule::class])
interface SettingsComponent : SettingsProvider {
  
  @Component.Builder
  interface Builder {
    
    @BindsInstance
    fun app(app: Application): Builder
    
    fun build(): SettingsComponent
  }
  
  companion object {
    
    fun createComponent(app: Application): SettingsComponent {
      return DaggerSettingsComponent.builder().app(app).build()
    }
  }
}

@Module
object SettingsModule {
  
  @Provides
  @Singleton
  @JvmStatic
  fun provideServiceStarter(app: Application): Settings {
    return AndroidSettings(SharedPrefsStorage(AndroidSettings.SETTINGS_FILENAME, app))
  }
}
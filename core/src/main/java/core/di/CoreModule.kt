package core.di

import androidx.lifecycle.ViewModelProvider
import core.di.viewmodel.ViewModelFactory
import core.util.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class CoreModule {
  
  @Binds
  abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
  
  @Module
  companion object {
    
    @JvmStatic
    @Provides
    fun provideDispatchersProvider(): DispatcherProvider = DispatcherProvider.DefaultImpl
  }
}
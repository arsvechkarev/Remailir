package core.di

import androidx.lifecycle.ViewModelProvider
import core.CoroutinesDispatcherProvider
import core.RxJavaSchedulersProvider
import core.di.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class CoreModule {
  
  @Provides
  fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory = factory
  
  @Provides
  fun provideCoroutinesDispatcherProvider(): CoroutinesDispatcherProvider =
    CoroutinesDispatcherProvider.DefaultImpl
  
  @Provides
  fun provideRxJavaSchedulersProvider(): RxJavaSchedulersProvider =
    RxJavaSchedulersProvider.DefaultImpl
}
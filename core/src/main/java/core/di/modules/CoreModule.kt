package core.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import core.CoroutinesDispatcherProvider
import core.RxJavaSchedulersProvider
import core.di.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
class CoreModule {
  
  @Provides
  fun bindViewModelFactory(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>)
      : ViewModelProvider.Factory {
    return ViewModelFactory(creators)
  }
  
  @Provides
  fun provideCoroutinesDispatcherProvider(): CoroutinesDispatcherProvider =
    CoroutinesDispatcherProvider.DefaultImpl
  
  @Provides
  fun provideRxJavaSchedulersProvider(): RxJavaSchedulersProvider =
    RxJavaSchedulersProvider.DefaultImpl
}
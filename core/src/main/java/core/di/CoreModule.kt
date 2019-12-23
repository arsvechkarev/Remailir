package core.di

import androidx.lifecycle.ViewModelProvider
import core.di.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class CoreModule {
  
  @Binds
  abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
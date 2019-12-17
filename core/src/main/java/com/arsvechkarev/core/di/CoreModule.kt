package com.arsvechkarev.core.di

import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.core.di.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class CoreModule {
  
  @Binds
  abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
package core.di

import core.Dispatchers

interface CoreDependenciesProvider {
  
  fun provideDispatchers(): Dispatchers
}
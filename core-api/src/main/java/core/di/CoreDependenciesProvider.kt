package core.di

import core.Dispatchers
import core.ThisUserInfoProvider
import core.ThisUserInfoStorage

interface CoreDependenciesProvider {
  
  fun provideDispatchers(): Dispatchers
  
  fun provideThisUserStorage(): ThisUserInfoStorage
  
  fun provideThisUserInfoProvider(): ThisUserInfoProvider
}
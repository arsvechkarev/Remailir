package core.di

import core.ServiceStarter

interface ServiceStarterProvider {
  
  fun provideServiceStarter(): ServiceStarter
}
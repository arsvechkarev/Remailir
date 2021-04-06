package com.arsvechkarev.remailir.di

import com.github.terrakok.cicerone.Cicerone
import dagger.Component
import dagger.Module
import dagger.Provides
import navigation.Router
import navigation.RouterProvider
import javax.inject.Singleton

@Singleton
@Component(modules = [RouterModule::class])
interface RouterComponent : RouterProvider {
  
  companion object {
    
    fun createComponent(): RouterComponent {
      val component = DaggerRouterComponent.create()
      RouterProvider.initialize(component)
      return component
    }
  }
}

@Module
object RouterModule {
  
  @Provides
  @Singleton
  @JvmStatic
  fun provideRouter(): Router = Router()
  
  @Provides
  @Singleton
  @JvmStatic
  fun provideCicerone(router: Router): Cicerone<Router> = Cicerone.create(router)
}
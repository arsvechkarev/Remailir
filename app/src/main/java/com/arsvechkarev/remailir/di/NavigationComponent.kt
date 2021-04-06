package com.arsvechkarev.remailir.di

import android.widget.FrameLayout
import com.arsvechkarev.remailir.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import moxy.MvpAppCompatActivity
import navigation.BaseScreen
import navigation.ExtendedNavigator
import navigation.ExtendedNavigatorImpl
import navigation.MvpViewScreenHandler
import navigation.OfClassNameFactory
import navigation.RouterProvider
import navigation.ScreenHandler
import navigation.ScreenHandlerFactory
import navigation.ViewNavigationHost
import javax.inject.Singleton

@Singleton
@Component(modules = [NavigationModule::class], dependencies = [RouterProvider::class])
interface NavigationComponent {
  
  fun inject(activity: MainActivity)
  
  @Component.Builder
  interface Builder {
    
    @BindsInstance
    fun activity(activity: MvpAppCompatActivity): Builder
    
    @BindsInstance
    fun rootViewId(id: Int): Builder
    
    fun routerProvider(routerProvider: RouterProvider): Builder
    
    fun build(): NavigationComponent
  }
  
  companion object {
    
    fun createComponent(
      activity: MvpAppCompatActivity,
      rootViewId: Int,
      routerProvider: RouterProvider
    ): NavigationComponent {
      return DaggerNavigationComponent.builder()
          .routerProvider(routerProvider)
          .activity(activity)
          .rootViewId(rootViewId)
          .build()
    }
  }
}

@Module
object NavigationModule {
  
  @Provides
  @JvmStatic
  fun provideNavigator(activity: MvpAppCompatActivity, rootViewId: Int): ExtendedNavigator {
    val rootView = activity.findViewById<FrameLayout>(rootViewId)
    val screenHandlerViewProvider = { handler: ScreenHandler -> (handler as MvpViewScreenHandler).view }
    val navHost = ViewNavigationHost(rootView, screenHandlerViewProvider)
    val screenHandlerFactory = ScreenHandlerFactory { screenKey, screen ->
      MvpViewScreenHandler(screen as BaseScreen, screenKey.toString(),
        activity.mvpDelegate, activity)
    }
    return ExtendedNavigatorImpl(navHost, OfClassNameFactory, screenHandlerFactory)
  }
}
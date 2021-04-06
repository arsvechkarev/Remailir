package com.arsvechkarev.home.di

import com.arsvechkarev.home.domain.HomeRepository
import com.arsvechkarev.home.presentation.HomePresenter
import core.Dispatchers
import core.di.AppDependenciesProvider
import core.di.CoreDependenciesProvider
import core.di.FeatureScope
import dagger.Component
import dagger.Module
import dagger.Provides
import firebase.chat.FirebaseChatRequestsDataSourceProvider
import navigation.Router
import navigation.RouterProvider

@FeatureScope
@Component(
  dependencies = [
    CoreDependenciesProvider::class,
    FirebaseChatRequestsDataSourceProvider::class,
    RouterProvider::class
  ],
  modules = [HomeModule::class]
)
interface HomeComponent {
  
  fun presenter(): HomePresenter
  
  companion object {
    
    fun getPresenter(): HomePresenter {
      return DaggerHomeComponent.builder()
          .coreDependenciesProvider(AppDependenciesProvider.instance)
          .firebaseChatRequestsDataSourceProvider(FirebaseChatRequestsDataSourceProvider.instance)
          .routerProvider(RouterProvider.instance)
          .build()
          .presenter()
    }
  }
}

@Module
object HomeModule {
  
  @Provides
  @FeatureScope
  @JvmStatic
  fun providePresenter(
    homeRepository: HomeRepository,
    router: Router,
    dispatchers: Dispatchers
  ): HomePresenter = HomePresenter(homeRepository, router, dispatchers)
}
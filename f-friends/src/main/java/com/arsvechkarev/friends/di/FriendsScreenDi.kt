package com.arsvechkarev.friends.di

import com.arsvechkarev.friends.domain.FriendsInteractor
import com.arsvechkarev.friends.domain.FriendsRepository
import com.arsvechkarev.friends.domain.FriendsRepositoryImpl
import com.arsvechkarev.friends.domain.FriendsScreensCommunicator
import com.arsvechkarev.friends.presentation.FriendsPresenter
import com.arsvechkarev.friends.presentation.pagerscreens.allfriends.AllFriendsPresenter
import com.arsvechkarev.friends.presentation.pagerscreens.myrequests.MyRequestsPresenter
import com.arsvechkarev.friends.presentation.pagerscreens.requeststome.RequestsToMePresenter
import core.Dispatchers
import core.StringToUserMapper
import core.ThisUserInfoStorage
import core.di.AppDependenciesProvider
import core.di.FeatureScope
import core.model.toUser
import dagger.Component
import dagger.Module
import dagger.Provides
import firebase.database.FirebaseDatabase
import firebase.database.FirebaseDatabaseDependenciesProvider
import firebase.database.UsersActions
import firebase.database.UsersDatabaseSchema
import navigation.RouterProvider

@FeatureScope
@Component(
  dependencies = [
    AppDependenciesProvider::class,
    RouterProvider::class,
    FirebaseDatabaseDependenciesProvider::class
  ],
  modules = [FriendsModule::class]
)
interface FriendsComponent {
  
  fun provideFriendsPresenter(): FriendsPresenter
  
  fun provideAllFriendsScreenPresenter(): AllFriendsPresenter
  
  fun provideMyRequestsScreenPresenter(): MyRequestsPresenter
  
  fun provideRequestsToMeScreenPresenter(): RequestsToMePresenter
  
  companion object {
    
    private var _instance: FriendsComponent? = null
    
    fun get(): FriendsComponent {
      if (_instance == null) {
        _instance = DaggerFriendsComponent.builder()
            .appDependenciesProvider(AppDependenciesProvider.instance)
            .routerProvider(RouterProvider.instance)
            .firebaseDatabaseDependenciesProvider(FirebaseDatabaseDependenciesProvider.instance)
            .build()
      }
      return _instance!!
    }
    
    fun clear() {
      _instance = null
    }
  }
}

@Module
object FriendsModule {
  
  @Provides
  @FeatureScope
  @JvmStatic
  fun provideRepository(
    thisUserInfoStorage: ThisUserInfoStorage,
    schema: UsersDatabaseSchema,
    firebaseDatabase: FirebaseDatabase,
    usersActions: UsersActions,
    userMapper: StringToUserMapper
  ): FriendsRepository {
    return FriendsRepositoryImpl(thisUserInfoStorage.getUserInfo().toUser(), schema,
      firebaseDatabase, usersActions, userMapper)
  }
  
  @Provides
  @FeatureScope
  @JvmStatic
  fun provideInteractor(dispatchers: Dispatchers, repository: FriendsRepository): FriendsInteractor {
    return FriendsInteractor(repository, dispatchers)
  }
  
  @Provides
  @FeatureScope
  @JvmStatic
  fun provideScreenCommunicator(): FriendsScreensCommunicator = FriendsScreensCommunicator()
}
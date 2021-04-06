package firebase.impl.di

import core.Dispatchers
import core.ThisUserInfoProvider
import core.di.CoreDependenciesProvider
import dagger.Component
import dagger.Module
import dagger.Provides
import firebase.chat.ChatsRequestsDataSource
import firebase.chat.FirebaseChatRequestsDataSourceProvider
import firebase.impl.ChatsRequestsDataSourceImpl
import javax.inject.Singleton

@Singleton
@Component(
  dependencies = [CoreDependenciesProvider::class],
  modules = [FirebaseChatsModule::class]
)
interface FirebaseChatsComponent : FirebaseChatRequestsDataSourceProvider {
  
  companion object {
    
    fun createComponent(
      coreDependenciesProvider: CoreDependenciesProvider
    ): FirebaseChatRequestsDataSourceProvider {
      val component = DaggerFirebaseChatsComponent.builder()
          .coreDependenciesProvider(coreDependenciesProvider)
          .build()
      FirebaseChatRequestsDataSourceProvider.initialize(component)
      return component
    }
  }
}

@Module
object FirebaseChatsModule {
  
  @Provides
  @Singleton
  @JvmStatic
  fun provideChatsDataSource(
    thisUserInfoProvider: ThisUserInfoProvider, dispatchers: Dispatchers
  ): ChatsRequestsDataSource {
    return ChatsRequestsDataSourceImpl(thisUserInfoProvider.getUserInfo().username, dispatchers)
  }
}
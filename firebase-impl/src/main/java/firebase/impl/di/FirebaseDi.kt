package firebase.impl

import firebase.database.ByUsernameUsersActions
import core.Dispatchers
import firebase.database.FirebaseDatabase
import firebase.database.UsersActions
import firebase.database.UsersDatabaseSchema
import core.di.CoreDependenciesProvider
import firebase.FirebaseDependenciesProvider
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(
  dependencies = [CoreDependenciesProvider::class],
  modules = [FirebaseModule::class]
)
interface FirebaseComponent : FirebaseDependenciesProvider {
  
  companion object {
    
    fun createComponent(coreDependenciesProvider: CoreDependenciesProvider): FirebaseComponent {
      return DaggerFirebaseComponent.builder()
          .coreDependenciesProvider(coreDependenciesProvider)
          .build()
    }
  }
}

@Module
object FirebaseModule {
  
  @Provides
  @Singleton
  @JvmStatic
  fun provideUserActions(): UsersActions = ByUsernameUsersActions
  
  @Provides
  @Singleton
  @JvmStatic
  fun provideUsersDatabaseSchema(): UsersDatabaseSchema = PathDatabaseSchema
  
  @Provides
  @Singleton
  @JvmStatic
  fun provideFirebaseDatabase(dispatchers: Dispatchers): FirebaseDatabase {
    return FirebaseDatabaseImpl(dispatchers)
  }
}
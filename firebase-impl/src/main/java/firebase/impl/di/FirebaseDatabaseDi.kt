package firebase.impl.di

import core.Dispatchers
import core.di.CoreDependenciesProvider
import dagger.Component
import dagger.Module
import dagger.Provides
import firebase.database.ByUsernameUsersActions
import firebase.database.FirebaseDatabase
import firebase.database.FirebaseDatabaseDependenciesProvider
import firebase.database.UsersActions
import firebase.database.UsersDatabaseSchema
import firebase.impl.FirebaseDatabaseImpl
import firebase.impl.PathDatabaseSchema
import javax.inject.Singleton

@Singleton
@Component(
  dependencies = [CoreDependenciesProvider::class],
  modules = [FirebaseDatabaseModule::class]
)
interface FirebaseDatabaseComponent : FirebaseDatabaseDependenciesProvider {
  
  companion object {
    
    fun createComponent(coreDependenciesProvider: CoreDependenciesProvider): FirebaseDatabaseComponent {
      val component = DaggerFirebaseDatabaseComponent.builder()
          .coreDependenciesProvider(coreDependenciesProvider)
          .build()
      FirebaseDatabaseDependenciesProvider.initialize(component)
      return component
    }
  }
}

@Module
object FirebaseDatabaseModule {
  
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
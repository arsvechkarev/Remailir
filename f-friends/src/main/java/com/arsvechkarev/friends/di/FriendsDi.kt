package com.arsvechkarev.friends.di

import com.arsvechkarev.core.UserMapper
import com.arsvechkarev.core.concurrency.AndroidDispatchers
import com.arsvechkarev.core.model.User
import com.arsvechkarev.firebase.auth.FirebaseAuthenticator
import com.arsvechkarev.firebase.database.FirebaseDatabase
import com.arsvechkarev.firebase.database.FirebaseDatabaseImpl
import com.arsvechkarev.firebase.database.PathDatabaseSchema
import com.arsvechkarev.friends.domain.ByUsernameUsersActions
import com.arsvechkarev.friends.domain.FriendsInteractor
import com.arsvechkarev.friends.domain.FriendsRepositoryImpl
import com.arsvechkarev.friends.domain.FriendsScreensCommunicator

object FriendsDi {
  
  // TODO (3/31/2021): Configure di properly
  var database: FirebaseDatabase = FirebaseDatabaseImpl(AndroidDispatchers)
  
  val friendsInteractor by lazy {
    FriendsInteractor(FriendsRepositoryImpl(
      User(FirebaseAuthenticator.getUsername()), PathDatabaseSchema,
      database, ByUsernameUsersActions, UserMapper
    ))
  }
  
  val friendsScreensBridge = FriendsScreensCommunicator()
}
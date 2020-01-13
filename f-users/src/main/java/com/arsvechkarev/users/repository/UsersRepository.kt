package com.arsvechkarev.users.repository

import com.google.firebase.firestore.FirebaseFirestore
import core.model.users.User
import durdinapps.rxfirebase2.RxFirestore
import firebase.schema.Collections.Users
import io.reactivex.Single
import log.Loggable
import log.log
import storage.AppUser
import javax.inject.Inject

class UsersRepository @Inject constructor(
  private val firestore: FirebaseFirestore
) : Loggable {
  
  override val logTag = "UsersRepository"
  
  fun fetchUsers(): Single<List<User>> {
    return RxFirestore.getCollection(firestore.collection(Users))
      .map { it.toObjects(User::class.java) }
      .flattenAsObservable { it }
      .filter { it.id != AppUser.get().id }
      .toList()
      .doOnSuccess {
        it.forEach {
          log { it }
        }
      }
  }
  
}
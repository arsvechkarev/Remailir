package com.arsvechkarev.users.repository

import com.google.firebase.firestore.FirebaseFirestore
import core.model.users.User
import durdinapps.rxfirebase2.RxFirestore
import firebase.schema.Collections.Users
import firebase.utils.thisUser
import io.reactivex.Single
import javax.inject.Inject

class UsersRepository @Inject constructor(
  private val firestore: FirebaseFirestore
) {
  
  fun fetchUsers(): Single<List<User>> {
    return RxFirestore.getCollection(firestore.collection(Users))
      .map { it.toObjects(User::class.java) }
      .flattenAsObservable { it }
      .filter { it.id != thisUser.uid }
      .toList()
  }
}
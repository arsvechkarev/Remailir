package com.arsvechkarev.users.repository

import com.google.firebase.firestore.FirebaseFirestore
import core.model.users.User
import durdinapps.rxfirebase2.RxFirestore
import firebase.schema.ChatModel.memberIds
import firebase.schema.ChatModel.otherUser
import firebase.schema.Collections.OneToOneChats
import firebase.schema.Collections.Users
import firebase.utils.calculateChatIdWith
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
  
  fun createChat(userToCreate: User): Single<User> {
    return Single.create { emitter ->
      val data = hashMapOf(
        memberIds to listOf(userToCreate.id, AppUser.get().id),
        otherUser to userToCreate
      )
      
      FirebaseFirestore.getInstance().collection(OneToOneChats)
        .document(calculateChatIdWith(userToCreate.id))
        .set(data)
        .addOnSuccessListener {
          emitter.onSuccess(userToCreate)
        }
        .addOnFailureListener {
          emitter.onError(it)
        }
    }
  }
}
package com.arsvechkarev.messaging.repository

import com.google.firebase.firestore.FirebaseFirestore
import core.model.users.User
import firebase.schema.ChatModel.memberIds
import firebase.schema.ChatModel.userOne
import firebase.schema.ChatModel.userTwo
import firebase.schema.Collections
import firebase.utils.calculateChatIdWith
import io.reactivex.Completable
import log.log
import storage.AppUser
import storage.SharedPreferencesManager
import javax.inject.Inject

class ChatMetadataCreator @Inject constructor(
  private val sharedPreferencesManager: SharedPreferencesManager
) {
  
  fun createChatMetadataIfNeeded(userToCreate: User): Completable {
    if (sharedPreferencesManager.isTrue(METADATA_ALREADY_CREATED)) {
      log { "Chat metadata already created" }
      return Completable.complete()
    }
    log { "Creating chat metadata" }
    return createChat(userToCreate)
      .doOnComplete { sharedPreferencesManager.putBoolean(METADATA_ALREADY_CREATED, true) }
  }
  
  private fun createChat(userToCreate: User): Completable {
    return Completable.create { emitter ->
      val data = hashMapOf(
        memberIds to listOf(userToCreate.id, AppUser.get().id),
        userOne to AppUser.get(),
        userTwo to userToCreate
      )
      
      FirebaseFirestore.getInstance().collection(Collections.OneToOneChats)
        .document(calculateChatIdWith(userToCreate.id))
        .set(data)
        .addOnSuccessListener {
          log { "Chat metadata created" }
          emitter.onComplete()
        }
        .addOnFailureListener {
          emitter.onError(it)
        }
    }
  }
  
  companion object {
    const val METADATA_ALREADY_CREATED = "METADATA_ALREADY_CREATED"
  }
}
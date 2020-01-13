package com.arsvechkarev.messaging.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query.Direction.ASCENDING
import core.model.messaging.DialogMessage
import core.model.users.User
import durdinapps.rxfirebase2.RxFirestore
import firebase.schema.Collections.Messages
import firebase.schema.Collections.OneToOneChats
import firebase.schema.MessageModel.timestamp
import firebase.utils.calculateChatIdWith
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import log.Loggable
import log.log
import javax.inject.Inject


class MessagingRepository @Inject constructor() : Loggable {
  
  override val logTag = "Messaging_Repository"
  
  private lateinit var otherUser: User
  
  private val query by lazy {
    FirebaseFirestore.getInstance().collection(OneToOneChats)
      .document(calculateChatIdWith(otherUser.id))
      .collection(Messages)
      .orderBy(timestamp, ASCENDING)
  }
  
  fun fetchMessages(otherUser: User): Flowable<MutableList<DialogMessage>> {
    this.otherUser = otherUser
    return RxFirestore.observeQueryRef(query, MetadataChanges.EXCLUDE, BackpressureStrategy.BUFFER)
      .map { it.toObjects(DialogMessage::class.java) }
      .doOnEach { messages ->
        messages.value?.forEach {
          log { "${it.fromUserId} -> ${it.toUserId}: ${it.text}" }
        }
      }
  }
  
  fun sendMessage(dialogMessage: DialogMessage): Completable {
    log { "Sending message" }
    return Completable.create { emmiter ->
      FirebaseFirestore.getInstance().collection(OneToOneChats)
        .document(calculateChatIdWith(otherUser.id))
        .collection(Messages)
        .add(dialogMessage)
        .addOnSuccessListener {
          log { "Sent message successfully" }
          emmiter.onComplete()
        }.addOnFailureListener {
          log(it) { "Error while sending a message" }
          emmiter.onError(it)
        }
    }
  }
}
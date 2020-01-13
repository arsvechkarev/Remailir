package com.arsvechkarev.messaging.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import core.extensions.currentTimestamp
import core.model.messaging.DialogMessage
import core.model.users.User
import durdinapps.rxfirebase2.RxFirestore
import firebase.schema.Collections
import firebase.schema.Collections.OneToOneChats
import firebase.schema.MessageModel
import firebase.utils.calculateChatIdWith
import io.reactivex.Maybe
import log.Loggable
import storage.AppUser
import javax.inject.Inject


class MessagingRepository @Inject constructor() : Loggable {
  
  override val logTag = "Messaging"
  
  private lateinit var otherUser: User
  
  fun fetchMessages(otherUser: User): Maybe<List<DialogMessage>> {
    this.otherUser = otherUser
    
    val query = FirebaseFirestore.getInstance().collection(OneToOneChats)
      .document(calculateChatIdWith(otherUser.id))
      .collection(Collections.Messages)
      .orderBy(MessageModel.timestamp, Query.Direction.ASCENDING)
    
    return RxFirestore.getCollection(query)
      .map { it.toObjects(DialogMessage::class.java) }
  }
  
  fun sendMessage(message: String) {
    val dialogMessage = DialogMessage(AppUser.get().id, otherUser.id, message, currentTimestamp())
    FirebaseFirestore.getInstance().collection(OneToOneChats)
      .document(calculateChatIdWith(otherUser.id))
      .collection(Collections.Messages)
      .add(dialogMessage)
  }
}
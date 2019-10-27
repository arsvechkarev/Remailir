package com.arsvechkarev.chat.repository

import com.arsvechkarev.core.model.OtherUser
import com.arsvechkarev.core.model.messaging.DialogMessage
import com.arsvechkarev.firebase.Schema
import com.arsvechkarev.firebase.getChatIdWith
import com.arsvechkarev.firebase.thisUserId
import com.google.firebase.Timestamp
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject


class ChatRepository @Inject constructor() {
  
  fun fetchMessages(otherUser: OtherUser, listener: EventListener<QuerySnapshot>): ListenerRegistration {
    return FirebaseFirestore.getInstance().collection(Schema.OneToOneChats)
      .document(getChatIdWith(otherUser))
      .collection(Schema.Collections.Messages)
      .orderBy(Schema.MessageModel.timestamp, Query.Direction.ASCENDING)
      .addSnapshotListener(listener)
  }
  
  fun sendMessage(
    message: String,
    otherUser: OtherUser,
    onSuccess: () -> Unit,
    onFailure: (Throwable) -> Unit
  ) {
    val dialogMessage = DialogMessage(thisUserId, otherUser.id, message, Timestamp.now().seconds)
    FirebaseFirestore.getInstance().collection(Schema.OneToOneChats)
      .document(getChatIdWith(otherUser))
      .collection(Schema.Collections.Messages)
      .add(dialogMessage)
      .addOnSuccessListener { onSuccess() }
      .addOnFailureListener(onFailure)
  }
}
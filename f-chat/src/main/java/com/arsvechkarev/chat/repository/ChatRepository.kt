package com.arsvechkarev.chat.repository

import com.arsvechkarev.core.model.Friend
import com.arsvechkarev.core.model.messaging.DialogMessage
import com.arsvechkarev.firebase.Schema
import com.arsvechkarev.firebase.getChatIdWith
import com.arsvechkarev.firebase.thisUserId
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject
import kotlin.concurrent.timerTask


class ChatRepository @Inject constructor() {
  
  fun fetchMessages(friend: Friend, listener: EventListener<QuerySnapshot>): ListenerRegistration {
    return FirebaseFirestore.getInstance().collection(Schema.OneToOneChats)
      .document(getChatIdWith(friend))
      .collection(Schema.Collections.Messages)
      .orderBy(Schema.MessageModel.timestamp, Query.Direction.ASCENDING)
      .addSnapshotListener(listener)
  }
  
  fun sendMessage(
    message: String,
    friend: Friend,
    onSuccess: () -> Unit,
    onFailure: (Throwable) -> Unit
  ) {
    val dialogMessage = DialogMessage(thisUserId, friend.id, message, Timestamp.now().seconds)
    FirebaseFirestore.getInstance().collection(Schema.OneToOneChats)
      .document(getChatIdWith(friend))
      .collection(Schema.Collections.Messages)
      .add(dialogMessage)
      .addOnSuccessListener { onSuccess() }
      .addOnFailureListener(onFailure)
  }
}
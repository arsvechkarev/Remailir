package com.arsvechkarev.chat.repository

import android.util.Log
import com.arsvechkarev.core.model.messaging.DialogMessage
import com.arsvechkarev.core.model.users.OtherUser
import com.arsvechkarev.firebase.Schema
import com.arsvechkarev.firebase.getChatIdWith
import com.arsvechkarev.firebase.thisUserId
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject


class ChatRepository @Inject constructor() {
  
  fun fetchMessages(
    otherUser: OtherUser,
    listener: EventListener<QuerySnapshot>
  ): ListenerRegistration {
    return FirebaseFirestore.getInstance().collection(Schema.Collections.OneToOneChats)
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
    val timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
    val dialogMessage = DialogMessage(thisUserId, otherUser.id, message, timestamp)
    val time = LocalDateTime.ofEpochSecond(timestamp, 0, ZonedDateTime.now().offset)
    Log.d("SendingMessage", "${time.hour} - ${time.minute} - ${time.second}")
    FirebaseFirestore.getInstance().collection(Schema.Collections.OneToOneChats)
      .document(getChatIdWith(otherUser))
      .collection(Schema.Collections.Messages)
      .add(dialogMessage)
      .addOnSuccessListener { onSuccess() }
      .addOnFailureListener(onFailure)
  }
}
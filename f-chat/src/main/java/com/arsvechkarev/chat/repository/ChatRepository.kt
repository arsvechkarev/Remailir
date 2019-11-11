package com.arsvechkarev.chat.repository

import android.util.Log
import com.arsvechkarev.core.model.messaging.DialogMessage
import com.arsvechkarev.core.model.users.OtherUser
import com.arsvechkarev.firebase.Collections
import com.arsvechkarev.firebase.MessageModel
import com.arsvechkarev.firebase.getChatIdWith
import com.arsvechkarev.firebase.thisUser
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
    addChatMetadata(otherUser)
    return FirebaseFirestore.getInstance().collection(Collections.OneToOneChats)
      .document(getChatIdWith(otherUser.id))
      .collection(Collections.Messages)
      .orderBy(MessageModel.timestamp, Query.Direction.ASCENDING)
      .addSnapshotListener(listener)
  }
  
  fun addChatMetadata(otherUser: OtherUser) {
    val data = hashMapOf(
      "memberIds" to listOf(otherUser.id, thisUser.uid),
      "otherUserId" to otherUser.id
    )
    FirebaseFirestore.getInstance().collection(Collections.OneToOneChats)
      .document(getChatIdWith(otherUser.id))
      .set(data)
  }
  
  fun sendMessage(
    message: String,
    otherUser: OtherUser,
    onSuccess: () -> Unit,
    onFailure: (Throwable) -> Unit
  ) {
    val timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
    val dialogMessage = DialogMessage(thisUser.uid, otherUser.id, message, timestamp)
    val time = LocalDateTime.ofEpochSecond(timestamp, 0, ZonedDateTime.now().offset)
    Log.d("SendingMessage", "${time.hour} - ${time.minute} - ${time.second}")
    FirebaseFirestore.getInstance().collection(Collections.OneToOneChats)
      .document(getChatIdWith(otherUser.id))
      .collection(Collections.Messages)
      .add(dialogMessage)
      .addOnSuccessListener { onSuccess() }
      .addOnFailureListener(onFailure)
  }
}
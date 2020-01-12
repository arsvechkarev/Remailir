package com.arsvechkarev.messages.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.DESCENDING
import core.model.messaging.Chat
import core.model.messaging.DialogMessage
import firebase.RetrieveListener
import firebase.schema.ChatModel.memberIds
import firebase.schema.Collections.Messages
import firebase.schema.Collections.OneToOneChats
import firebase.schema.MessageModel.timestamp
import firebase.utils.getChatIdWith
import firebase.utils.thisUser
import log.Loggable
import javax.inject.Inject

class MessagesRepository @Inject constructor(
  private val firestore: FirebaseFirestore
) : Loggable {
  
  override val logTag: String = "MessagesRepository"
  
  fun fetchMessages(block: RetrieveListener<List<Chat>>.() -> Unit) {
    val holder = RetrieveListener<List<Chat>>().apply(block)
    firestore.collection(OneToOneChats)
      .whereArrayContains(memberIds, thisUser.uid)
      .get()
      .addOnSuccessListener { snapshot ->
        val chats = snapshot.toObjects(Chat::class.java)
        chats.forEach { chat ->
          firestore.collection(OneToOneChats)
            .document(getChatIdWith(chat.otherUser.id))
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
              if (documentSnapshot!!.exists()) {
                Log.d("firefire", "doc = $documentSnapshot")
                documentSnapshot.reference
                  .collection(Messages)
                  .orderBy(timestamp, DESCENDING)
                  .limit(1)
                  .get()
                  .addOnSuccessListener {
                    val messages = it.toObjects(DialogMessage::class.java)
                    val dialogMessage = if (messages.size > 0) messages[0] else null
                    chats.add(Chat(chat.otherUser, dialogMessage))
                    holder.success(chats)
                  }
              }
            }
        }
      }
  }
  
}

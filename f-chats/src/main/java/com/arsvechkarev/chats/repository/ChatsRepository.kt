package com.arsvechkarev.chats.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.DESCENDING
import core.model.messaging.Chat
import core.model.messaging.DialogMessage
import firebase.schema.ChatModel.memberIds
import firebase.schema.Collections.Messages
import firebase.schema.Collections.OneToOneChats
import firebase.schema.MessageModel.timestamp
import firebase.utils.calculateChatIdWith
import io.reactivex.Single
import log.Loggable
import storage.AppUser
import javax.inject.Inject

class ChatsRepository @Inject constructor(
  private val firestore: FirebaseFirestore
) : Loggable {
  
  override val logTag: String = "MessagesRepository"
  
  fun fetchChats(): Single<List<Chat>> {
    return Single.create { emitter ->
      firestore.collection(OneToOneChats)
        .whereArrayContains(memberIds, AppUser.get().id)
        .get()
        .addOnSuccessListener { snapshot ->
          val chats = snapshot.toObjects(Chat::class.java)
          chats.forEach { chat ->
            firestore.collection(OneToOneChats)
              .document(calculateChatIdWith(chat.otherUser.id))
              .addSnapshotListener { documentSnapshot, _ ->
                if (documentSnapshot!!.exists()) {
                  documentSnapshot.reference
                    .collection(Messages)
                    .orderBy(timestamp, DESCENDING)
                    .limit(1)
                    .addSnapshotListener { snapshot, _ ->
                      val messages = snapshot!!.toObjects(DialogMessage::class.java)
                      val dialogMessage = if (messages.size > 0) messages[0] else null
                      chats.add(Chat(chat.otherUser, dialogMessage))
                      emitter.onSuccess(chats)
                    }
                }
              }
          }
        }.addOnFailureListener {
          emitter.onError(it)
        }
    }
  }
  
}

package com.arsvechkarev.chats.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.DESCENDING
import core.model.messaging.Chat
import core.model.messaging.DialogMessage
import core.model.messaging.PartialChat
import firebase.schema.ChatModel.memberIds
import firebase.schema.Collections.Messages
import firebase.schema.Collections.OneToOneChats
import firebase.schema.MessageModel.timestamp
import firebase.utils.calculateChatIdWith
import firebase.utils.getOtherUser
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import log.Loggable
import log.log
import storage.AppUser
import javax.inject.Inject

class ChatsRepository @Inject constructor(
  private val firestore: FirebaseFirestore
) : Loggable {
  
  override val logTag: String = "MessagesRepository"
  
  fun fetchChatsRx(): Flowable<List<Chat>> {
    return Flowable.create({ emitter ->
      firestore.collection(OneToOneChats)
        .whereArrayContains(memberIds, AppUser.get().id)
        .get()
        .addOnSuccessListener { snapshot ->
          if (snapshot.isEmpty) {
            if (!emitter.isCancelled) {
              emitter.onComplete()
              return@addOnSuccessListener
            }
          }
          val partialChats = snapshot.toObjects(PartialChat::class.java)
          log { "partialChats = ${partialChats.size} + $partialChats" }
          partialChats.forEach { pChat ->
            firestore.collection(OneToOneChats)
              .document(calculateChatIdWith(pChat.userOne.id, pChat.userTwo.id))
              .collection(Messages)
              .orderBy(timestamp, DESCENDING)
              .limit(1)
              .addSnapshotListener { snapshot, _ ->
                val chats = ArrayList<Chat>()
                log { "partialChats = ${partialChats.size} + $partialChats" }
                val messages = snapshot!!.toObjects(DialogMessage::class.java)
                log { "messages = $messages" }
                val dialogMessage = if (messages.size > 0) messages[0] else null
                val otherUser = getOtherUser(pChat.userOne, pChat.userTwo)
                log { "otherUser = $otherUser" }
                chats.add(Chat(otherUser, dialogMessage))
                log { "chats, ${chats.size} = $chats" }
                if (!emitter.isCancelled) {
                  emitter.onNext(chats)
                }
              }
          }
        }.addOnFailureListener {
          if (!emitter.isCancelled) {
            emitter.onError(it)
          }
        }
    }, BackpressureStrategy.BUFFER)
  }
  
  //  fun fetchChatsRx2(): Flowable<List<Chat>> {
  //    return Flowable.create({ emitter ->
  //      firestore.collection(OneToOneChats)
  //        .whereArrayContains(memberIds, AppUser.get().id)
  //        .addSnapshotListener { snapshot, _ ->
  //          if (snapshot?.isEmpty == true) {
  //            emitter.onNext(listOf())
  //          }
  //          val partialChats = snapshot?.toObjects(PartialChat::class.java)
  //          log { "partialChats = ${partialChats?.size} + $partialChats" }
  //          partialChats?.forEach { pChat ->
  //            firestore.collection(OneToOneChats)
  //              .document(calculateChatIdWith(pChat.otherUser.id))
  //              .collection(Messages)
  //              .orderBy(timestamp, DESCENDING)
  //              .limit(1)
  //              .addSnapshotListener { snapshot, _ ->
  //                val chats = ArrayList<Chat>()
  //                log { "partialChats = ${partialChats.size} + $partialChats" }
  //                val messages = snapshot!!.toObjects(DialogMessage::class.java)
  //                log { "messages = $messages" }
  //                val dialogMessage = if (messages.size > 0) messages[0] else null
  //
  //
  //                chats.add(Chat(pChat.otherUser, dialogMessage))
  //                log { "chats, ${chats.size} = $chats" }
  //                emitter.onNext(chats)
  //              }
  //          }
  //        }
  //    }, BackpressureStrategy.BUFFER)
  //  }
  
}

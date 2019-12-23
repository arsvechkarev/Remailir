package com.arsvechkarev.messages.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.DESCENDING
import core.model.messaging.Chat
import core.model.messaging.DialogMessage
import core.model.messaging.PartialChat
import firebase.Collections.Messages
import firebase.Collections.OneToOneChats
import firebase.MessageModel.timestamp
import firebase.getChatIdWith
import firebase.thisUser

class MessagesRepository {
  
  class ListenersHolder<S> {
    
    lateinit var successBlock: (S) -> Unit
    lateinit var failureBlock: (Throwable) -> Unit
    
    fun onSuccess(successBlock: (S) -> Unit) {
      this.successBlock = successBlock
    }
    
    fun onFailure(failureBlock: (Throwable) -> Unit) {
      this.failureBlock = failureBlock
    }
    
  }
  
  fun fetchMessages(block: ListenersHolder<List<Chat>>.() -> Unit) {
    val holder = ListenersHolder<List<Chat>>().apply(block)
    FirebaseFirestore.getInstance()
      .collection(OneToOneChats)
      .whereArrayContains("memberIds", thisUser.uid)
      .get()
      .addOnSuccessListener { snapshot ->
        val chats = ArrayList<Chat>()
        val partialChats = snapshot.toObjects(PartialChat::class.java)
        partialChats.forEach { partialChat ->
          FirebaseFirestore.getInstance()
            .collection(OneToOneChats)
            .document(getChatIdWith(partialChat.otherUser.id))
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
                    chats.add(Chat(partialChat.otherUser, dialogMessage))
                    holder.successBlock(chats)
                  }
              }
            }
        }
      }
  }
  
}

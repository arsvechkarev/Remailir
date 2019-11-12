package com.arsvechkarev.messages.repository

import android.util.Log
import com.arsvechkarev.core.base.repos.ListenersHolder
import com.arsvechkarev.core.model.Chat
import com.arsvechkarev.core.model.PartialChat
import com.arsvechkarev.core.model.messaging.DialogMessage
import com.arsvechkarev.firebase.Collections.Messages
import com.arsvechkarev.firebase.Collections.OneToOneChats
import com.arsvechkarev.firebase.MessageModel.timestamp
import com.arsvechkarev.firebase.getChatIdWith
import com.arsvechkarev.firebase.thisUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.DESCENDING

class MessagesRepository {
  
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
            //            .whereLessThan("id", thisUser.uid)
            //            .whereGreaterThan("id", thisUser.uid)
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

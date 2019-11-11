package com.arsvechkarev.messages.repository

import com.arsvechkarev.core.base.repos.ListenersHolder
import com.arsvechkarev.core.model.Chat
import com.arsvechkarev.core.model.PartialChat
import com.arsvechkarev.core.model.messaging.DialogMessage
import com.arsvechkarev.core.model.users.OtherUser
import com.arsvechkarev.firebase.Collections
import com.arsvechkarev.firebase.MessageModel
import com.arsvechkarev.firebase.getChatIdWith
import com.arsvechkarev.firebase.thisUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MessagesRepository {
  
  fun fetchMessages(block: ListenersHolder<List<Chat>>.() -> Unit) {
    val holder = ListenersHolder<List<Chat>>().apply(block)
    FirebaseFirestore.getInstance()
      .collection(Collections.OneToOneChats)
      .whereArrayContains("memberIds", thisUser.uid)
      .get()
      .addOnSuccessListener { snapshot ->
        val partialChats = snapshot.toObjects(PartialChat::class.java)
        val otherUsersList = ArrayList<OtherUser>()
        partialChats.forEach { chat ->
          FirebaseFirestore.getInstance()
            .collection(Collections.Users)
            .whereEqualTo("id", chat.otherUserId)
            .get()
            .addOnSuccessListener { querySnapshot ->
              otherUsersList.addAll(querySnapshot.toObjects(OtherUser::class.java))
              val chats = ArrayList<Chat>()
              otherUsersList.forEach {
                FirebaseFirestore.getInstance()
                  .collection(Collections.OneToOneChats)
                  .document(getChatIdWith(it.id))
                  .collection(Collections.Messages)
                  .orderBy(MessageModel.timestamp, Query.Direction.DESCENDING)
                  .limit(1)
                  .get()
                  .addOnSuccessListener { snapshot ->
                    val lastMessage = if (snapshot.size() > 0) {
                      snapshot?.toObjects(DialogMessage::class.java)?.get(0)
                    } else {
                      null
                    }
                    chats.add(Chat(it, lastMessage))
                    holder.successBlock(chats)
                  }
              }
            }
          
        }
      }
  }
  
}

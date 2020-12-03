package com.arsvechkarev.firebase.firestore.chatting

import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.extenstions.await
import com.arsvechkarev.core.model.Message
import com.arsvechkarev.firebase.firestore.FirestoreSchema.chatDocumentName
import com.arsvechkarev.firebase.firestore.FirestoreSchema.chats
import com.arsvechkarev.firebase.firestore.FirestoreSchema.messages
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.withContext

class FirebaseChattingDataSource(
  private val thisUserUsername: String,
  private val otherUserUsername: String,
  private val dispatchers: Dispatchers
) : ChattingDataSource {
  
  private var listenerRegistration: ListenerRegistration? = null
  
  private val instance = FirebaseFirestore.getInstance()
  
  override suspend fun sendMessage(message: Message): Unit = withContext(dispatchers.IO) {
    val map = mapOf<String, Any>(
      "id" to message.id,
      "text" to message.text,
      "sender" to message.sender,
      "timestamp" to message.timestamp,
    )
    instance.collection(chats)
        .document(chatDocumentName(thisUserUsername, otherUserUsername))
        .collection(messages)
        .add(map)
        .await()
  }
  
  override suspend fun listenForMessages(listener: MessageListener) {
    listenerRegistration = instance.collection(chats)
        .document(chatDocumentName(thisUserUsername, otherUserUsername))
        .collection(messages)
        .orderBy("timestamp", Query.Direction.DESCENDING)
        .addSnapshotListener { value, error ->
          if (error == null && value != null) {
            if (value.documents.isNotEmpty()) {
              val snapshot = value.documents[0]
              val message = Message(
                id = snapshot.getString("id")!!,
                text = snapshot.getString("text")!!,
                sender = snapshot.getString("sender")!!,
                timestamp = snapshot.getLong("timestamp")!!
              )
              listener.receiveMessage(message)
            }
          }
        }
  }
  
  override fun releaseListener() {
    listenerRegistration?.remove()
    listenerRegistration = null
  }
}
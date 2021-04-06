package firebase.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import core.Dispatchers
import core.utils.await
import firebase.FirestoreSchema.chatDocumentName
import firebase.FirestoreSchema.chats
import firebase.FirestoreSchema.chattingUserFieldName
import firebase.FirestoreSchema.chatting_prefix
import firebase.FirestoreSchema.messages
import firebase.FirestoreSchema.participants
import firebase.FirestoreSchema.statusFieldName
import firebase.FirestoreSchema.status_chatting
import firebase.FirestoreSchema.status_not_chatting
import firebase.chat.ChatMetaInfoDataSource
import firebase.chat.ChatWaitingListener
import firebase.chat.ChatsRequestsDataSource
import kotlinx.coroutines.withContext

class ChatsRequestsDataSourceImpl(
  private val thisUserUsername: String,
  private val dispatchers: Dispatchers
) : ChatsRequestsDataSource, ChatMetaInfoDataSource {
  
  private val instance = FirebaseFirestore.getInstance()
  
  private var alreadyJoined = false
  private var registrationListener: ListenerRegistration? = null
  
  override suspend fun waitForJoining(
    otherUserUsername: String,
    listener: ChatWaitingListener,
  ) = withContext(dispatchers.IO) {
    val chatDocumentName = chatDocumentName(thisUserUsername, otherUserUsername)
    createChatDocumentIfNotExists(otherUserUsername)
    registrationListener = instance.collection(chats)
        .document(chatDocumentName)
        .addSnapshotListener lb@{ snapshot, error ->
          if (error != null) {
            return@lb
          }
          require(snapshot != null)
          val value = snapshot.getString(statusFieldName(otherUserUsername))
          if (value == status_chatting) {
            listener.onUserBecameActive()
            alreadyJoined = true
          } else if (value == status_not_chatting && alreadyJoined) {
            listener.onUserBecameInactive()
          }
          //          val value = snapshot!!.getBoolean(chattingUserFieldName(otherUserUsername))!!
          //          if (value) {
          //            listener.onUserBecameActive()
          //            alreadyJoined = true
          //          } else if (alreadyJoined) {
          //            listener.onUserBecameInactive()
          //          }
        }
  }
  
  override suspend fun setCurrentUserAsActive(otherUserUsername: String) {
    createChatDocumentIfNotExists(otherUserUsername)
    instance.collection(chats)
        .document(chatDocumentName(thisUserUsername, otherUserUsername))
        .update(statusFieldName(thisUserUsername), status_chatting)
        .await()
  }
  
  override suspend fun exitChat(otherUserUsername: String) {
    val chatDocumentRef = instance.collection(chats)
        .document(chatDocumentName(thisUserUsername, otherUserUsername))
    val document = chatDocumentRef
        .get()
        .await() ?: return
    chatDocumentRef
        .update(statusFieldName(thisUserUsername), status_not_chatting)
        .await()
    if (document.getString(statusFieldName(otherUserUsername)) == status_not_chatting) {
      // Other user is not active, exit chat and delete messages
      chatDocumentRef.collection(messages)
          .get()
          .await()?.documents?.forEach { documentSnapshot ->
            chatDocumentRef.collection(messages)
                .document(documentSnapshot.id).delete().await()
          }
    }
  }
  
  override fun releaseJoiningListener() {
    registrationListener?.remove()
    registrationListener = null
  }
  
  override suspend fun getCurrentlyWaitingForChat(): List<String> = withContext(dispatchers.IO) {
    val snapshot = instance.collection(chats)
        .whereArrayContains(participants, thisUserUsername)
        .get()
        .await() ?: return@withContext emptyList()
    val waitingForChatting = ArrayList<String>()
    for (snapshotElement in snapshot) {
      // Despite being nested loop, it will run only several
      // times just to check whether other user is active
      for ((key, value) in snapshotElement.data) {
        if (key.startsWith(chatting_prefix) && !key.endsWith(thisUserUsername)) {
          if (value as Boolean) {
            waitingForChatting.add(key.drop(chatting_prefix.length))
            continue
          }
        }
      }
    }
    return@withContext waitingForChatting
  }
  
  override suspend fun respondToChatRequest(
    otherUserUsername: String
  ): Boolean = withContext(dispatchers.IO) lb@{
    val document = instance.collection(chats)
        .document(chatDocumentName(thisUserUsername, otherUserUsername))
        .get()
        .await()
    val isOtherActive = document!!.getBoolean(chattingUserFieldName(otherUserUsername))!!
    if (isOtherActive) {
      val map = document.data ?: return@lb false
      map[chattingUserFieldName(thisUserUsername)] = true
      instance.collection(chats)
          .document(chatDocumentName(thisUserUsername, otherUserUsername))
          .update(map)
          .await()
      return@lb true
    }
    return@lb false
  }
  
  private suspend fun createChatDocumentIfNotExists(otherUserUsername: String) {
    val chatDocumentName = chatDocumentName(thisUserUsername, otherUserUsername)
    val document = instance.collection(chats)
        .document(chatDocumentName)
        .get()
        .await()
    if (document == null || !document.exists()) {
      instance.collection(chats)
          .document(chatDocumentName)
          .set(mapOf<String, Any>(
            statusFieldName(thisUserUsername) to status_not_chatting,
            statusFieldName(otherUserUsername) to status_not_chatting,
            "$chatting_prefix$thisUserUsername" to false,
            "$chatting_prefix$otherUserUsername" to false,
            participants to arrayListOf(thisUserUsername, otherUserUsername)
          ))
          .await()
    }
  }
}
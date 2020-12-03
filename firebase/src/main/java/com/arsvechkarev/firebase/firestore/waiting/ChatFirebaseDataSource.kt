package com.arsvechkarev.firebase.firestore.waiting

import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.extenstions.await
import com.arsvechkarev.firebase.firestore.FirestoreSchema.activeUserFieldName
import com.arsvechkarev.firebase.firestore.FirestoreSchema.active_prefix
import com.arsvechkarev.firebase.firestore.FirestoreSchema.chatDocumentName
import com.arsvechkarev.firebase.firestore.FirestoreSchema.chats
import com.arsvechkarev.firebase.firestore.FirestoreSchema.participants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.withContext
import timber.log.Timber

class ChatFirebaseDataSource(
  private val thisUserUsername: String,
  private val dispatchers: Dispatchers
) : ChatRequestsDataSource, ChatWaitingDataSource {
  
  private val instance = FirebaseFirestore.getInstance()
  
  private var alreadyJoined = false
  private var registrationListener: ListenerRegistration? = null
  private var chatWaitingListener: ChatWaitingListener? = null
  
  override suspend fun setCurrentUserAsActive(otherUserUsername: String) {
    instance.collection(chats)
        .document(chatDocumentName(thisUserUsername, otherUserUsername))
        .update(activeUserFieldName(thisUserUsername), true)
        .await()
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
        if (key.startsWith(active_prefix) && !key.endsWith(thisUserUsername)) {
          if (value as Boolean) {
            waitingForChatting.add(key.drop(active_prefix.length))
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
    val documentSnapshot = instance.collection(chats)
        .document(chatDocumentName(thisUserUsername, otherUserUsername))
        .get()
        .await() ?: return@lb false
    val isOtherActive = documentSnapshot.getBoolean(activeUserFieldName(otherUserUsername))!!
    if (isOtherActive) {
      val map = documentSnapshot.data ?: return@lb false
      map[activeUserFieldName(thisUserUsername)] = true
      instance.collection(chats)
          .document(chatDocumentName(thisUserUsername, otherUserUsername))
          .update(map)
          .await()
      return@lb true
    }
    return@lb false
  }
  
  override suspend fun waitForJoining(
    otherUserUsername: String,
    listener: ChatWaitingListener,
  ) = withContext(dispatchers.IO) {
    chatWaitingListener = listener
    registrationListener = instance.collection(chats)
        .document(chatDocumentName(thisUserUsername, otherUserUsername))
        .addSnapshotListener lb@{ snapshot, error ->
          error?.printStackTrace()
          Timber.d(error)
          if (error == null) {
            val value = snapshot!!.getBoolean(activeUserFieldName(otherUserUsername))!!
            if (value) {
              chatWaitingListener?.onUserJoined()
              alreadyJoined = true
            } else if (alreadyJoined) {
              chatWaitingListener?.onUserCancelledRequest()
            }
          }
        }
  }
  
  override fun releaseJoiningListener() {
    registrationListener?.remove()
    registrationListener = null
    chatWaitingListener = null
  }
}